package ru.vdovin.product_price_parser.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "category")
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @OneToMany(mappedBy =  "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subcategory> subcategories;

    public List<Subcategory> getActiveSubcategories() {
        return getSubcategories().stream().filter(Subcategory::isActive).collect(Collectors.toList());
    }
}
