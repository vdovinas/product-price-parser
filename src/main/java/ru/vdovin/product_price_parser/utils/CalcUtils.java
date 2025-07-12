package ru.vdovin.product_price_parser.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CalcUtils {

    public static Long calcDiscount(Long oldPrice, Long newPrice) {
        return 100 - newPrice * 100 / oldPrice;
    }
}
