package ru.vdovin.product_price_parser.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.vdovin.product_price_parser.enums.Source;

@Getter
@Setter
@Accessors(chain = true)
public class BaseProduct {
    private String id;
    private String name;
    private Source source;
    private Long newPrice;
    private Long oldPrice;
    private String link;
}
