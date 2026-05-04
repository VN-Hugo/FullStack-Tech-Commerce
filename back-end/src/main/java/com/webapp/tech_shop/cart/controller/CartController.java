package com.webapp.tech_shop.cart.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.tech_shop.cart.CartService;
import com.webapp.tech_shop.cart.dto.AddToCartRequest;
import com.webapp.tech_shop.cart.dto.CartResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import java.util.*;

import com.webapp.tech_shop.user.User;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    
    private final CartService cartService;

    @GetMapping("/me")
    public ResponseEntity<CartResponse> getCart(
        @AuthenticationPrincipal User user) {
    
        return ResponseEntity.ok(
            cartService.getCartOfCurrentUser(user.getId())
        );
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(
        @AuthenticationPrincipal User user,
        @Valid @RequestBody AddToCartRequest request) {
        cartService.addToCart(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Void> updateQuantity(
            @PathVariable UUID itemId, 
            @RequestParam Integer quantity) {
        
        cartService.updateQuantity(itemId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable UUID itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
