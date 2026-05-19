package com.webapp.tech_shop.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.webapp.tech_shop.payment.model.PaymentMethodType;
import com.webapp.tech_shop.payment.model.PaymentStatus;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        PaymentMethodType paymentMethod,
        PaymentStatus status,
        BigDecimal amount,
        String transactionId,
        LocalDateTime createdAt
) {
}
