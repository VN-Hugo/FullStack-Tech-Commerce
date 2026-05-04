package com.webapp.tech_shop.cart.exception;

import java.util.UUID;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(UUID productId, Integer requestedQuantity, Integer availableStock) {
        super("Insufficient stock for product " + productId + 
              ". Requested: " + requestedQuantity + ", Available: " + availableStock);
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
