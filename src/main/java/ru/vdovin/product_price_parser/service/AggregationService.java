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
import ru.vdovin.product_price_parser.service.parser.WbParserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.vdovin.product_price_parser.utils.CalcUtils.calcDiscount;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private final CategoryRepository categoryRepository;
    private final WbParserService wbParserService;
    private final KafkaProducer kafkaProducer;
    private final ProductService productService;
    private final TransactionTemplate transactionTemplate;

    public void processCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        categories.forEach(this::processCategory);
    }

    public void processCategory(Category category) {
        // 1. Загружаю свежие данные из всех источников
        List<BaseProduct> products = loadProducts(category);

        // 2. Сравнить новые значения с предыдущими, определить товары по которым цена снизилась
        List<BaseProduct> productsForNotification = filterProductsByDiscount(products, category);

        transactionTemplate.executeWithoutResult(transactionStatus -> {

            // 3. Удалить предыдущие значения по категории
            productService.deleteProductByCategory(category);

            // 4. Сохранить новые значения
            productService.save(products, category);

            // 5. Сформировать уведомления при изменении суммы
            productsForNotification.forEach(product ->
                    kafkaProducer.send(
                            product,
                            category.getCode()
                    ));
        });
    }

    private List<BaseProduct> loadProducts(Category category) {
        // Пока только ВБ
        return loadProductsBySource(category, Source.WB);
    }

    private List<BaseProduct> loadProductsBySource(Category category, Source source) {
        List<BaseProduct> products = wbParserService.loadProducts(category); // TODO: научить выбирать сервис по source

        List<String> ids = products.stream().map(BaseProduct::getId).toList();

        Map<String, Product> dbProductsByExternalIdMap = productService.findByExternalIdInAndSource(ids, source).stream()
                .collect(Collectors.toMap(Product::getExternalId, product -> product));

        products.stream()
                .filter(product -> dbProductsByExternalIdMap.containsKey(product.getId()))
                .forEach(product -> product.setOldPrice(dbProductsByExternalIdMap.get(product.getId()).getPrice()));

        return products;
    }

    private List<BaseProduct> filterProductsByDiscount(List<BaseProduct> products, Category category) {
        return products.stream()
                .filter(product -> product.getOldPrice() != null)
//                .filter(product -> product.getOldPrice() > product.getNewPrice())
                .filter(product -> calcDiscount(product.getOldPrice(), product.getNewPrice()) > category.getMinDiscountPercent())
                .toList();
    }
}
