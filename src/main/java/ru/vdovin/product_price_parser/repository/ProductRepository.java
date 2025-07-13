package ru.vdovin.product_price_parser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vdovin.product_price_parser.model.entity.Subcategory;
import ru.vdovin.product_price_parser.model.entity.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByExternalIdInAndSourceId(List<String> externalIds, Long sourceId);

    void deleteProductBySubcategory(Subcategory subcategory);
}
