package com.webapp.tech_shop.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
    UUID itemId,
    UUID productId,
    String productName,
    Integer quantity,
    String productImageUrl,
    BigDecimal price,
    BigDecimal totalPrice
) {}
