package com.webapp.tech_shop.cart.dto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
public record CartResponse(
    UUID cartId,
    List<CartItemResponse> items,
    Integer totalProducts, 
    BigDecimal totalPrice  
) {}