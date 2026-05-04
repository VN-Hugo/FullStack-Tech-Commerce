package com.webapp.tech_shop.cart.exception;

import java.util.UUID;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(UUID itemId) {
        super("Cart item not found: " + itemId);
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
