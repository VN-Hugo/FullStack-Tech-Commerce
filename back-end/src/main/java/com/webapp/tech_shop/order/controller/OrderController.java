package com.webapp.tech_shop.order.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.tech_shop.order.OrderService;
import com.webapp.tech_shop.order.dto.CreateOrderRequest;
import com.webapp.tech_shop.order.dto.OrderDetailResponse;
import com.webapp.tech_shop.order.dto.OrderItemResponse;
import com.webapp.tech_shop.order.model.Order;
import com.webapp.tech_shop.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrderRequest request) {

        Order order = orderService.createOrder(user.getId(), request);
        return ResponseEntity.ok(toResponse(order));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderDetailResponse>> getMyOrders(
            @AuthenticationPrincipal User user) {

        List<OrderDetailResponse> response = orderService.getOrdersByCustomer(user.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID orderId) {

        Order order = orderService.getOrderById(user.getId(), orderId);
        return ResponseEntity.ok(toResponse(order));
    }

    private OrderDetailResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems() != null
                ? order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductId(),
                                item.getProductName(),
                                item.getPrice(),
                                item.getQuantity(),
                                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return new OrderDetailResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getAddressShipping(),
                order.getPhoneNumber(),
                order.getCustomerName(),
                order.getCreatedAt(),
                items);
    }
}

