package ru.vdovin.product_price_parser.model.dto.wb;

import lombok.Getter;

import java.util.List;

@Getter
public class WbSearchResponseDTO {

    private List<ProductDTO> products;

    @Getter
    public static class ProductDTO {
        private String id;
        private String name;
        private List<ProductSizesDTO> sizes;
    }

    @Getter
    public static class ProductSizesDTO {
        private ProductPriceDTO price;
    }

    @Getter
    public static class ProductPriceDTO {
        private Long product;
    }
}
