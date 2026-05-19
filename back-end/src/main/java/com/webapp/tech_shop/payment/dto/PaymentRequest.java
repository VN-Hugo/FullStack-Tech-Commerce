package com.webapp.tech_shop.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.webapp.tech_shop.payment.model.PaymentMethodType;

import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull(message = "Order ID is required")
        UUID orderId,
        @NotNull(message = "Payment method is required")
        PaymentMethodType paymentMethod,
        @NotNull(message = "Amount is required")
        BigDecimal amount
) {
}
