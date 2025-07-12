package ru.vdovin.product_price_parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vdovin.product_price_parser.enums.Source;
import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.model.entity.Category;
import ru.vdovin.product_price_parser.model.entity.Product;
import ru.vdovin.product_price_parser.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findByExternalIdInAndSource(List<String> externalIds, Source source) {
        return productRepository.findByExternalIdInAndSource(externalIds, source);
    }

    public void deleteProductByCategory(Category category) {
        productRepository.deleteProductByCategory(category);
    }

    public void save(List<BaseProduct> products, Category category) {
        productRepository.saveAll(products.stream().map(product -> mapToEntity(product, category)).toList());
    }

    private Product mapToEntity(BaseProduct product, Category category) {
        Product entity = new Product();

        entity.setName(product.getName());
        entity.setExternalId(product.getId());
        entity.setSource(product.getSource());
        entity.setPrice(product.getNewPrice());
        entity.setLastUpdated(LocalDateTime.now());
        entity.setCategory(category);

        return entity;
    }
}
