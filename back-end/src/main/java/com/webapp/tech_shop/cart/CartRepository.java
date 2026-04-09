package com.webapp.tech_shop.cart;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public class CartRepository extends JpaRepository<Cart, Long> {
    
}
