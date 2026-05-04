package com.webapp.tech_shop.cart.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String message) {
        super(message);
    }

    public InvalidQuantityException(Integer quantity) {
        super("Invalid quantity: " + quantity + ". Quantity must be greater than 0");
    }
}
