package com.webapp.tech_shop.cart;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.webapp.tech_shop.shared.BaseEntity;

import java.util.List;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
@Builder
public class Cart extends BaseEntity {
    private Integer totalProduct;
    private BigDecimal totalPrice;
    private UUID customerId;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
}
