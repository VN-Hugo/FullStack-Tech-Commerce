package com.webapp.tech_shop.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import com.webapp.tech_shop.cart.dto.CartResponse;
import com.webapp.tech_shop.cart.dto.AddToCartRequest;
import com.webapp.tech_shop.cart.dto.CartItemResponse;
import com.webapp.tech_shop.cart.mapper.CartMapper;
import com.webapp.tech_shop.exception.BaseException;
import com.webapp.tech_shop.exception.ErrorCode;
import com.webapp.tech_shop.product.ProductService;
import com.webapp.tech_shop.product.dto.ProductInfoForOrder;


import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartMapper cartMapper;


    private Cart createCartEntity(UUID userId) {
        return cartRepository.findByCustomerId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomerId(userId);
                    newCart.setTotalProduct(0);
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
    }
    
   // 1. Lấy giỏ hàng (Kỹ thuật gộp dữ liệu giữa 2 module)
    public CartResponse getCartOfCurrentUser(UUID userId) {
        if (userId == null) {
            throw new BaseException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        Cart cart = createCartEntity(userId);
        List<CartItem> items = cart.getCartItems();

        if (items.isEmpty()) {
            return new CartResponse(cart.getId(), Collections.emptyList(), 0, BigDecimal.ZERO);
        }

        // Lấy danh sách ID sản phẩm có trong giỏ
        List<UUID> productIds = items.stream()
                .map(CartItem::getProductId)
                .toList();

        // Gọi sang Product Module để lấy thông tin chi tiết (Chỉ lấy field cần thiết)
        // Đây là điểm giao tiếp duy nhất giữa 2 module
        List<ProductInfoForOrder> productInfos = productService.getProductsForOrder(productIds);
        
        // Tạo Map để tra cứu thông tin sản phẩm cho nhanh
        Map<UUID, ProductInfoForOrder> productMap = productInfos.stream()
                .collect(Collectors.toMap(ProductInfoForOrder::id, p -> p));

        // Build danh sách Item Response
        List<CartItemResponse> itemResponses = items.stream()
                .map(item -> cartMapper.toCartItemResponse(item, productMap))
                .toList();

        return new CartResponse(cart.getId(), itemResponses, cart.getTotalProduct(), cart.getTotalPrice());
    }

    // add product to cart
    @Transactional
    public void addToCart(UUID userId, AddToCartRequest request) {
        validateAddToCartRequest(userId, request);
        
        Cart cart = createCartEntity(userId);
        
        // Verify product exists
        List<ProductInfoForOrder> products = productService.getProductsForOrder(List.of(request.productId()));
        if (products.isEmpty()) {
            throw new BaseException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(request.productId());
            newItem.setQuantity(request.quantity());
            cartItemRepository.save(newItem);
        }

        syncCartTotals(cart);
    }

    // Update quantity
    @Transactional
    public void updateQuantity(UUID itemId, Integer newQuantity) {
        if (itemId == null) {
            throw new BaseException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        if (newQuantity == null || newQuantity <= 0) {
            throw new BaseException(ErrorCode.INVALID_QUANTITY);
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new BaseException(ErrorCode.CART_ITEM_NOT_FOUND));

        item.setQuantity(newQuantity);
        cartItemRepository.save(item);
        syncCartTotals(item.getCart());
    }

    @Transactional
    public void removeItem(UUID itemId) {
        if (itemId == null) {
            throw new BaseException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new BaseException(ErrorCode.CART_ITEM_NOT_FOUND));
        
        Cart cart = item.getCart();
        cartItemRepository.delete(item);
        syncCartTotals(cart);
    }

    private void syncCartTotals(Cart cart) {
        List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
        
        if (items.isEmpty()) {
            cart.setTotalProduct(0);
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepository.save(cart);
            return;
        }

        List<UUID> pIds = items.stream().map(CartItem::getProductId).toList();
        Map<UUID, BigDecimal> priceMap = productService.getProductsForOrder(pIds).stream()
                .collect(Collectors.toMap(ProductInfoForOrder::id, ProductInfoForOrder::price));

        int totalQty = items.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = items.stream()
                .map(item -> {
                    BigDecimal price = priceMap.getOrDefault(item.getProductId(), BigDecimal.ZERO);
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalProduct(totalQty);
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
    }

    private void validateAddToCartRequest(UUID userId, AddToCartRequest request) {
        if (userId == null) {
            throw new BaseException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        if (request == null) {
            throw new BaseException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        if (request.productId() == null) {
            throw new BaseException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new BaseException(ErrorCode.INVALID_QUANTITY);
        }
    }
}