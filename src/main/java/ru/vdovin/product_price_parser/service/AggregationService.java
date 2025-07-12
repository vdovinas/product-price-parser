package ru.vdovin.product_price_parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.vdovin.product_price_parser.configuration.kafka.KafkaProducer;
import ru.vdovin.product_price_parser.enums.Source;
import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.model.entity.Category;
import ru.vdovin.product_price_parser.model.entity.Product;
import ru.vdovin.product_price_parser.repository.CategoryRepository;
import ru.vdovin.product_price_parser.repository.ProductRepository;
import ru.vdovin.product_price_parser.service.parser.WbParserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private final CategoryRepository categoryRepository;
    private final WbParserService wbParserService;
    private final KafkaProducer kafkaProducer;
    private final ProductRepository productRepository;
    private final TransactionTemplate transactionTemplate;

    public void processCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        categories.forEach(this::processCategory);
    }

    public void processCategory(Category category) {
        // 1. Загружаю свежие данные из всех источников (пока только ВБ)
        List<BaseProduct> products = wbParserService.loadProducts(category);

        // 2. Получить предыдущие данные, сохраненные в БД
        List<String> ids = products.stream().map(BaseProduct::getId).toList();
        Map<String, Product> dbProductsByExternalIdMap = productRepository.findByExternalIdInAndSource(ids, Source.WB).stream()
                .collect(Collectors.toMap(Product::getExternalId, product -> product));

        fillProductOldPrice(products, dbProductsByExternalIdMap);

        // 3. Сравнить новые значения с предыдущими, определить товары по которым цена снизилась
        List<BaseProduct> productsForNotification = filterProductsByDiscount(products, category);

        transactionTemplate.executeWithoutResult(transactionStatus -> {

            // 4. Удалить предыдущие значения по категории и источнику
            productRepository.deleteProductByCategoryAndSource(category, Source.WB);

            // 5. Сохранить новые значения
            productRepository.saveAll(products.stream().map(product -> mapToEntity(product, category)).toList());

            // 6. Сформировать уведомления при изменении суммы
            productsForNotification.forEach(product -> {
                kafkaProducer.send(
                        product,
                        category.getCode()
                );
            });
        });
    }

    private Product mapToEntity(BaseProduct product, Category category) {
        Product entity = new Product();

        entity.setName(product.getName());
        entity.setExternalId(product.getId());
        entity.setSource(Source.WB);
        entity.setPrice(product.getNewPrice());
        entity.setLastUpdated(LocalDateTime.now());
        entity.setCategory(category);

        return entity;
    }

    private void fillProductOldPrice(List<BaseProduct> products, Map<String, Product> dbProductsByExternalIdMap) {
        products.stream()
                .filter(product -> dbProductsByExternalIdMap.containsKey(product.getId()))
                .forEach(product -> product.setOldPrice(dbProductsByExternalIdMap.get(product.getId()).getPrice()));
    }

    private List<BaseProduct> filterProductsByDiscount(List<BaseProduct> products, Category category) {
        return products.stream()
                .filter(product -> product.getOldPrice() != null)
//                .filter(product -> product.getOldPrice() > product.getNewPrice())
                .filter(product -> calcChangingPercent(product.getOldPrice(), product.getNewPrice()) > category.getMinDiscountPercent())
                .toList();
    }

    // TODO: вынести мб в ютилити класс
    private Long calcChangingPercent(Long oldPrice, Long newPrice) {
        return 100 - newPrice * 100 / oldPrice;
    }
}
