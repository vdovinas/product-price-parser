package ru.vdovin.product_price_parser.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vdovin.product_price_parser.model.dto.SubcategoryDTO;
import ru.vdovin.product_price_parser.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryCode}/subcategories")
    @Operation(summary = "Получить перечень актуальных подкатегорий по коду категории")
    public List<SubcategoryDTO> getSubcategoriesByCategory(@PathVariable("categoryCode") String categoryCode) {
        return categoryService.getSubcategoryDTOsByCategoryCode(categoryCode);
    }
}
