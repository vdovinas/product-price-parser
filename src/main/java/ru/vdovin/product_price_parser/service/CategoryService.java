package ru.vdovin.product_price_parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vdovin.product_price_parser.model.dto.SubcategoryDTO;
import ru.vdovin.product_price_parser.model.entity.Subcategory;
import ru.vdovin.product_price_parser.repository.SubcategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final SubcategoryRepository subcategoryRepository;

    public List<Subcategory> getAllActiveSubcategories() {
        return subcategoryRepository.findByIsActiveTrue();
    }

    public List<SubcategoryDTO> getSubcategoryDTOsByCategoryCode(String categoryCode) {
        return subcategoryRepository.findByIsActiveTrueAndCategoryCode(categoryCode).stream()
                .map(this::buildDTO)
                .toList();
    }

    private SubcategoryDTO buildDTO(Subcategory subcategory) {
        return new SubcategoryDTO()
                .setId(subcategory.getId())
                .setName(subcategory.getName());
    }
}
