package ru.vdovin.product_price_parser.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "s_source")
@Getter
public class Source {
    @Id
    private Long id;

    private String name;
}
