package ru.vdovin.product_price_parser.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.vdovin.product_price_parser.enums.SourceType;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class BaseProduct {
    private String id;
    private String name;
    private SourceType source;
    private Long newPrice;
    private Long oldPrice;
    private String link;
    private String subcategory;
}
