package ru.vdovin.product_price_parser.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "subcategory")
@Getter
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isActive;

    @OneToMany(mappedBy =  "subcategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CategoryMetaData> metaData;

    private Integer minDiscountPercent;

    private Integer pageCount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
