package com.webapp.tech_shop.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotBlank(message = "Shipping address must not be empty")
        String addressShipping,
        @NotBlank(message = "Phone number must not be empty")
        String phoneNumber,
        @NotBlank(message = "Customer name must not be empty")
        String customerName
) {
}
