package com.webapp.tech_shop.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
public record UpdateProductRequest(
    String name,
    String pictureUrl,
    @Min(value = 0, message = "Quantity must be a positive number") Integer quantity,
    @Positive(message = "Price must be a positive number") BigDecimal price
)  {}
