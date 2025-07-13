package ru.vdovin.product_price_parser.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Соответствует таблице s_source
 */
@Getter
@RequiredArgsConstructor
public enum SourceType {
    WB(1L);

    private final Long id;
}
