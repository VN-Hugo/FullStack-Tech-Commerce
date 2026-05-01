package com.webapp.tech_shop.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Product Errors
    PRODUCT_NOT_FOUND("PRODUCT_001", "Product Not Found", "The requested product does not exist", HttpStatus.NOT_FOUND),
    // Authentication Errors
    INVALID_CREDENTIALS("AUTH_001", "Invalid Credentials", "The provided credentials areinvalid", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS("AUTH_002", "User Already Exists", "A user with the provided email already exists", HttpStatus.BAD_REQUEST),
    // Order Errors
    ORDER_NOT_FOUND("ORDER_001", "Order Not Found", "The requested order does not exist", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK("ORDER_002", "Insufficient Stock", "The requested product quantity is not enough", HttpStatus.BAD_REQUEST),
    // Cart Errors 
    CART_NOT_FOUND("CART_001", "Cart Not Found", "The requested cart does not exist", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("CART_002", "Cart Item Not Found", "The requested cart item does not exist", HttpStatus.NOT_FOUND),
    // Generic Erors
    UNCATEGORIZED_EXCEPTION("9999", "Uncategorized Error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    private final String code;
    private final String title;
    private final String message;
    private final HttpStatus status;
}