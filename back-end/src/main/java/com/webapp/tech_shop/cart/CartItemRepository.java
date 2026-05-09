package com.webapp.tech_shop.cart;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.tech_shop.cart.model.CartItem;

import java.util.*;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
    List<CartItem> findAllByCartId(UUID cartId);
    void deleteByCartId(UUID cartId);
} 
