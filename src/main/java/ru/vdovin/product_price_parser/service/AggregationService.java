package ru.vdovin.product_price_parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.vdovin.product_price_parser.configuration.kafka.KafkaProducer;
import ru.vdovin.product_price_parser.enums.SourceType;
import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.model.entity.Subcategory;
import ru.vdovin.product_price_parser.model.entity.Product;
import ru.vdovin.product_price_parser.repository.SubcategoryRepository;
import ru.vdovin.product_price_parser.service.parser.WbParserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.vdovin.product_price_parser.utils.CalcUtils.calcDiscount;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private final SubcategoryRepository subcategoryRepository;
    private final WbParserService wbParserService;
    private final KafkaProducer kafkaProducer;
    private final ProductService productService;
    private final TransactionTemplate transactionTemplate;

    public void processSubcategories() {
        List<Subcategory> subcategories = subcategoryRepository.findByIsActiveTrue();
        subcategories.forEach(this::processSubcategory);
    }

    public void processSubcategory(Subcategory subcategory) {
        // 1. Загружаю свежие данные из всех источников
        List<BaseProduct> products = loadProducts(subcategory);

        // 2. Сравнить новые значения с предыдущими, определить товары по которым цена снизилась
        List<BaseProduct> productsForNotification = filterProductsByDiscount(products, subcategory);

        transactionTemplate.executeWithoutResult(transactionStatus -> {

            // 3. Удалить предыдущие значения по категории
            productService.deleteProductBySubcategory(subcategory);

            // 4. Сохранить новые значения
            productService.save(products, subcategory);

            // 5. Сформировать уведомления при изменении суммы
            productsForNotification.forEach(product ->
                    kafkaProducer.send(
                            product,
                            subcategory.getCategory().getCode()
                    ));
        });
    }

    private List<BaseProduct> loadProducts(Subcategory subcategory) {
        // Пока только ВБ
        return loadProductsBySource(subcategory, SourceType.WB);
    }

    private List<BaseProduct> loadProductsBySource(Subcategory subcategory, SourceType sourceType) {
        List<BaseProduct> products = wbParserService.loadProducts(subcategory); // TODO: научить выбирать сервис по source

        List<String> ids = products.stream().map(BaseProduct::getId).toList();

        Map<String, Product> dbProductsByExternalIdMap = productService.findByExternalIdInAndSource(ids, sourceType).stream()
                .collect(Collectors.toMap(Product::getExternalId, product -> product));

        products.stream()
                .filter(product -> dbProductsByExternalIdMap.containsKey(product.getId()))
                .forEach(product -> product.setOldPrice(dbProductsByExternalIdMap.get(product.getId()).getPrice()));

        return products;
    }

    private List<BaseProduct> filterProductsByDiscount(List<BaseProduct> products, Subcategory subcategory) {
        return products.stream()
                .filter(product -> product.getOldPrice() != null)
//                .filter(product -> product.getOldPrice() > product.getNewPrice())
                .filter(product -> calcDiscount(product.getOldPrice(), product.getNewPrice()) > subcategory.getMinDiscountPercent())
                .toList();
    }
}
