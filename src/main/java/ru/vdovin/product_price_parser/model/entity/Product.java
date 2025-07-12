package ru.vdovin.product_price_parser.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.vdovin.product_price_parser.enums.Source;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // Название товара

    private String externalId;     // ID товара в маркетплейсе

    private Long price;   // Текущая цена

    @Enumerated(EnumType.STRING)
    private Source source;            // Источник товара

    private LocalDateTime lastUpdated; // Дата последнего обновления

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
