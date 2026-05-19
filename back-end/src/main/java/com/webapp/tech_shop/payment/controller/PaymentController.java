package com.webapp.tech_shop.payment.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.tech_shop.payment.PaymentService;
import com.webapp.tech_shop.payment.dto.PaymentRequest;
import com.webapp.tech_shop.payment.dto.PaymentResponse;
import com.webapp.tech_shop.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.getPayment(user.getId(), paymentId);
        return ResponseEntity.ok(response);
    }
}
