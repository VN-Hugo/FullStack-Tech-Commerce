package com.webapp.tech_shop.cart;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {


    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.customerId = :customerId")
    Optional<Cart> findByCustomerIdWithItems(@Param("customerId") UUID customerId);

    Optional<Cart> findByCustomerId(UUID customerId);
}