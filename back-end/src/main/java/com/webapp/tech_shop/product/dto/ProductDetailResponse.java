package com.webapp.tech_shop.product.dto;

import java.math.BigDecimal;
import java.util.UUID;
import com.webapp.tech_shop.product.model.ProductStatus;
public record ProductDetailResponse(
    UUID id,
    String name,
    String pictureUrl,
    Integer quantity,
    BigDecimal price,
    ProductStatus status
) {}
