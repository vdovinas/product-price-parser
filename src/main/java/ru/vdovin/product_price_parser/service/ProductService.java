package ru.vdovin.product_price_parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vdovin.product_price_parser.enums.SourceType;
import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.model.entity.Subcategory;
import ru.vdovin.product_price_parser.model.entity.Product;
import ru.vdovin.product_price_parser.repository.ProductRepository;
import ru.vdovin.product_price_parser.repository.SourceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final SourceRepository sourceRepository;

    public List<Product> findByExternalIdInAndSource(List<String> externalIds, SourceType sourceType) {
        return repository.findByExternalIdInAndSourceId(externalIds, sourceType.getId());
    }

    private void deleteProductBySubcategory(Subcategory subcategory) {
        repository.deleteProductBySubcategory(subcategory);
    }

    public void merge(List<BaseProduct> products, Subcategory subcategory) {

        // Удаление всех ранее сохраненных данных по подкатегории
        deleteProductBySubcategory(subcategory);

        // Сохранение новых данных
        repository.saveAll(products.stream().map(product -> mapToEntity(product, subcategory)).toList());
    }

    private Product mapToEntity(BaseProduct product, Subcategory subcategory) {
        Product entity = new Product();

        entity.setName(product.getName());
        entity.setExternalId(product.getId());
        entity.setSource(sourceRepository.findById(product.getSource().getId()).orElseThrow());
        entity.setPrice(product.getNewPrice());
        entity.setLastUpdatedDate(LocalDateTime.now());
        entity.setSubcategory(subcategory);

        return entity;
    }
}
