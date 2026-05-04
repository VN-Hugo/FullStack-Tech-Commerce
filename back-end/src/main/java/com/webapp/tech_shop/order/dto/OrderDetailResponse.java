package com.webapp.tech_shop.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.webapp.tech_shop.order.model.OrderStatus;

public record OrderDetailResponse(
        UUID id,
        OrderStatus status,
        BigDecimal totalPrice,
        String addressShipping,
        String phoneNumber,
        String customerName,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
