package ru.vdovin.product_price_parser.service.parser;

import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.model.entity.Subcategory;

import java.util.List;

public interface ParserService {

    List<BaseProduct> loadProducts(Subcategory subcategory);

}
