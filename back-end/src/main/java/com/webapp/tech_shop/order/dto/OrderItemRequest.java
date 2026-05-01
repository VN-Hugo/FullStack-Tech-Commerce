package com.webapp.tech_shop.order.dto;

public record OrderItemRequest(
    String productId,
    Integer quantity
) {
}