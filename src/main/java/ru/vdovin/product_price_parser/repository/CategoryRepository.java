package ru.vdovin.product_price_parser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vdovin.product_price_parser.model.entity.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findByIsActiveTrue();
}
