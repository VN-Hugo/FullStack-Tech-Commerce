package com.webapp.tech_shop.cart.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.tech_shop.cart.CartService;
import com.webapp.tech_shop.cart.dto.AddToCartRequest;
import com.webapp.tech_shop.cart.dto.CartResponse;
import com.webapp.tech_shop.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@Tag(name = "Cart Management", description = "APIs for managing the shopping cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/me")
    @Operation(summary = "Get current cart", description = "Retrieve the authenticated user's current cart")
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCartOfCurrentUser(user.getId()));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the authenticated user's cart")
    public ResponseEntity<Void> addToCart(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddToCartRequest request) {
        cartService.addToCart(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/items/{itemId}")
    @Operation(summary = "Update cart item quantity", description = "Update the quantity of a specific cart item")
    public ResponseEntity<Void> updateQuantity(
            @Parameter(description = "Cart item ID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID itemId,
            @Parameter(description = "New quantity", required = true) @RequestParam Integer quantity) {
        cartService.updateQuantity(itemId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the authenticated user's cart")
    public ResponseEntity<Void> removeItem(
            @Parameter(description = "Cart item ID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
