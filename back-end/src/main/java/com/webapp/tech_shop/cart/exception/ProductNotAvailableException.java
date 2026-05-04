package com.webapp.tech_shop.cart.exception;

import java.util.UUID;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException(UUID productId) {
        super("Product not available or not found: " + productId);
    }

    public ProductNotAvailableException(UUID productId, String reason) {
        super("Product " + productId + " is not available: " + reason);
    }
}
