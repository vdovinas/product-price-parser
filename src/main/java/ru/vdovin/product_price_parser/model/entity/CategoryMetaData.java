package ru.vdovin.product_price_parser.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "catagory_meta_data")
@Getter
public class CategoryMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "catagory_id", nullable = false)
    private Category category;

    private String code;

    private String value;

    private String description;
}
