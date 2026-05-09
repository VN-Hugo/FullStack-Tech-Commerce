package com.webapp.tech_shop.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSearchCriteria(
        String name,
        BigDecimal priceMin,
        BigDecimal priceMax,
        UUID brandId,
        String brandName,
        UUID categoryId,
        String categoryName) {
}
