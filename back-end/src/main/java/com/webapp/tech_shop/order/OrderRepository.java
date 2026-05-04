package com.webapp.tech_shop.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.tech_shop.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByCustomerId(UUID customerId);
    Optional<Order> findByIdAndCustomerId(UUID id, UUID customerId);
}