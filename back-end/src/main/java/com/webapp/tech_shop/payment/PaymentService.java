package com.webapp.tech_shop.payment;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tech_shop.exception.BaseException;
import com.webapp.tech_shop.exception.ErrorCode;
import com.webapp.tech_shop.order.OrderRepository;
import com.webapp.tech_shop.order.model.Order;
import com.webapp.tech_shop.order.model.OrderStatus;
import com.webapp.tech_shop.payment.dto.PaymentRequest;
import com.webapp.tech_shop.payment.dto.PaymentResponse;
import com.webapp.tech_shop.payment.model.Payment;
import com.webapp.tech_shop.payment.model.PaymentMethod;
import com.webapp.tech_shop.payment.model.PaymentMethodType;
import com.webapp.tech_shop.payment.model.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public PaymentResponse processPayment(UUID customerId, PaymentRequest request) {
        Order order = orderRepository.findByIdAndCustomerId(request.orderId(), customerId)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.IN_PROCESS) {
            throw new BaseException(ErrorCode.ORDER_CANNOT_CONFIRM);
        }

        BigDecimal expectedAmount = order.getTotalPrice() != null ? order.getTotalPrice() : BigDecimal.ZERO;
        if (request.amount() == null || request.amount().compareTo(expectedAmount) != 0) {
            throw new BaseException(ErrorCode.PAYMENT_INVALID_AMOUNT);
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findByMethod(request.paymentMethod().name())
                .orElseGet(() -> paymentMethodRepository.save(new PaymentMethod(request.paymentMethod().name())));

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(paymentMethod)
                .status(PaymentStatus.PAID)
                .amount(request.amount())
                .transactionId(UUID.randomUUID().toString())
                .build();

        Payment saved = paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID customerId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndOrderCustomerId(paymentId, customerId)
                .orElseThrow(() -> new BaseException(ErrorCode.PAYMENT_NOT_FOUND));
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                PaymentMethodType.valueOf(payment.getPaymentMethod().getMethod()),
                payment.getStatus(),
                payment.getAmount(),
                payment.getTransactionId(),
                payment.getCreatedAt());
    }
}
