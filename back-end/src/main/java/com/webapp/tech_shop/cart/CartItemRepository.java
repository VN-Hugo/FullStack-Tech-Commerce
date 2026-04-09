package com.webapp.tech_shop.cart;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    // Tìm kiếm CartItem theo Cart ID và Product ID
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
    List<CartItem> findAllByCartId(UUID cartId);
    // Xóa tất cả item thuộc về một giỏ hàng cụ thể
    void deleteByCartId(UUID cartId);
} 
