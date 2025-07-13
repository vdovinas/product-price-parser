package ru.vdovin.product_price_parser.repository;


import org.springframework.data.repository.CrudRepository;
import ru.vdovin.product_price_parser.model.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
