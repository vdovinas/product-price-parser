package ru.vdovin.product_price_parser.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class SubcategoryDTO {
    @Schema(description = "Идентификатор подкатегории")
    private Long id;
    @Schema(description = "Наименование подкатегории")
    private String name;
}
