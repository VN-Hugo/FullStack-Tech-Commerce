package com.webapp.tech_shop.cart.dto;
import java.util.UUID;

public record AddToCartRequest(
    UUID productId,
    Integer quantity
){}