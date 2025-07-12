package ru.vdovin.product_price_parser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.service.AggregationService;
import ru.vdovin.product_price_parser.service.parser.WbParserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WbParseController {

    private final WbParserService wbParserService;
    private final AggregationService aggregationService;

    @GetMapping(value = "/wb/category/{id}")
    public List<BaseProduct> getWbSearchResponseDTO(@PathVariable("id") Long id) {
        return wbParserService.search(id);
    }

    @GetMapping(value = "/test/kafka")
    public void testkafka() {
        aggregationService.processCategories();
    }

}
