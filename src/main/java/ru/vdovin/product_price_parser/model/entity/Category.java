package ru.vdovin.product_price_parser.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private boolean isActive;

    @OneToMany(mappedBy =  "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoryMetaData> metaData;

    private Integer minDiscountPercent;
}
