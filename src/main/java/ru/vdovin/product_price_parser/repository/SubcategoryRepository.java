package ru.vdovin.product_price_parser.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.vdovin.product_price_parser.model.entity.Subcategory;

import java.util.List;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {

    List<Subcategory> findByIsActiveTrue();

    @Query("from Subcategory s where s.category.code = :code and s.isActive = true")
    List<Subcategory> findByIsActiveTrueAndCategoryCode(String code);
}
