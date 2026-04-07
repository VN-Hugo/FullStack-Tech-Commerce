package com.webapp.tech_shop.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductInfoForOrder(
    UUID id,
    BigDecimal price,
    String name,
    Integer quantity
) {
}