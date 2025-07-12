package ru.vdovin.product_price_parser.service.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.vdovin.product_price_parser.client.WbClient;
import ru.vdovin.product_price_parser.enums.Source;
import ru.vdovin.product_price_parser.model.dto.BaseProduct;
import ru.vdovin.product_price_parser.model.dto.wb.WbQueryDTO;
import ru.vdovin.product_price_parser.model.dto.wb.WbSearchResponseDTO;
import ru.vdovin.product_price_parser.model.entity.Category;
import ru.vdovin.product_price_parser.model.entity.CategoryMetaData;
import ru.vdovin.product_price_parser.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WbParserService implements ParserService {

    private final WbClient wbClient;

    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<BaseProduct> search(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        return loadProducts(category);
    }

    @SneakyThrows
    public List<BaseProduct> loadProducts(Category category) {
        Map<String, Object> query = buildWbQuery(category);

        List<WbSearchResponseDTO.ProductDTO> products = new ArrayList<>();
        for (int i = 1; i <= 5; i++) { // TODO: количество страниц вынести в настройки категории
            query.put("page", i);
            products.addAll(wbClient.search(query).getProducts());

            Thread.sleep(1000); // Защита от 429 ошибки. Не будет работать, если будет нужна параллельная обработка
        }

        return products.stream().map(this::buildBaseProduct).toList();
    }

    private BaseProduct buildBaseProduct(WbSearchResponseDTO.ProductDTO product) {
        return new BaseProduct()
                .setId(product.getId())
                .setName(product.getName())
                .setSource(Source.WB)
                .setNewPrice(product.getSizes().get(0).getPrice().getProduct())
                .setLink(buildLink(product));
    }

    @SneakyThrows
    private Map<String, Object> buildWbQuery(Category category) {
        WbQueryDTO wbQueryDTO = new WbQueryDTO()
                .setQuery(category.getName());

        Map<String, Object> queryMap = objectMapper.convertValue(wbQueryDTO, new TypeReference<>() {});

        if (category.getMetaData() != null && !category.getMetaData().isEmpty()) {
            queryMap.putAll(
                    category.getMetaData().stream()
                            .collect(Collectors.toMap(CategoryMetaData::getCode, CategoryMetaData::getValue))
            );
        }

        return queryMap;
    }

    private String buildLink(WbSearchResponseDTO.ProductDTO product) {
        return String.format("https://www.wildberries.ru/catalog/%s/detail.aspx", product.getId());
    }

}
