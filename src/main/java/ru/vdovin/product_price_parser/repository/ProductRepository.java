package ru.vdovin.product_price_parser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vdovin.product_price_parser.enums.Source;
import ru.vdovin.product_price_parser.model.entity.Category;
import ru.vdovin.product_price_parser.model.entity.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByExternalIdInAndSource(List<String> externalIds, Source source);

    void deleteProductByCategory(Category category);
}
