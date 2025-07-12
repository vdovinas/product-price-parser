package ru.vdovin.product_price_parser.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vdovin.product_price_parser.model.dto.wb.WbSearchResponseDTO;

import java.util.Map;

@FeignClient(name = "WbClient", url = "https://search.wb.ru/")
public interface WbClient {

    @GetMapping("/exactmatch/ru/common/v14/search")
    WbSearchResponseDTO search(@SpringQueryMap Map<String, Object> queryMap);
}
