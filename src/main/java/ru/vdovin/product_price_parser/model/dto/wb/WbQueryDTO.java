package ru.vdovin.product_price_parser.model.dto.wb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WbQueryDTO {
    @JsonProperty("ab_search_filter")
    private final String searchFilter = "consists";

    private final Integer appType = 1;

    private final String curr = "rub";

    private final String dest = "12358357"; // Геолокация - Пермь

    private final Integer fpremium = 1; // Премиум - продавец

    private final String lang = "ru";

    private Integer page;

    private String query;

    private final String resultset = "catalog";

    private final String sort = "popular";
}
