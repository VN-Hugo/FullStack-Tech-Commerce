package com.webapp.tech_shop.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import com.webapp.tech_shop.cart.dto.CartResponse;
import com.webapp.tech_shop.cart.dto.AddToCartRequest;
import com.webapp.tech_shop.cart.dto.CartItemResponse;
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


    private Cart CreateCartEntity(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomerId(userId);
                    newCart.setTotalproduct(0);
                    newCart.setTotalprice(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
    }
    
   // 1. Lấy giỏ hàng (Kỹ thuật gộp dữ liệu giữa 2 module)
    public CartResponse getCartOfCurrentUser(UUID userId) {
         Cart cart = CreateCartEntity(userId);
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
                .map(item -> {
                    ProductInfoForOrder pInfo = productMap.get(item.getProductId());
                    BigDecimal price = (pInfo != null) ? pInfo.price() : BigDecimal.ZERO;
                    String name = (pInfo != null) ? pInfo.name() : "Sản phẩm không tồn tại";
                    
                    return new CartItemResponse(
                            item.getId(),
                            item.getProductId(),
                            name,
                            item.getQuantity(),
                            null,
                            price,
                            price.multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                }).toList();

        return new CartResponse(cart.getId(), itemResponses, cart.getTotalproduct(), cart.getTotalprice());
    }

    // add product to cart
    @Transactional
    public void addToCart(UUID userId, AddToCartRequest request) { 
        Cart cart = CreateCartEntity(userId);
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(request.productId()); // Chỉ lưu ID
            newItem.setQuantity(request.quantity());
            cartItemRepository.save(newItem);
        }

        syncCartTotals(cart);
    }

    // Update quantity
    @Transactional
    public void updateQuantity(UUID itemId, Integer newQuantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (newQuantity <= 0) {
            removeItem(itemId);
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
            syncCartTotals(item.getCart());
        }
    }

    @Transactional
    public void removeItem(UUID itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Cart cart = item.getCart();
        cartItemRepository.delete(item);
        syncCartTotals(cart);
    }

    private void syncCartTotals(Cart cart) {
        List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
        
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

        cart.setTotalproduct(totalQty);
        cart.setTotalprice(totalPrice);
        cartRepository.save(cart);
    }
}