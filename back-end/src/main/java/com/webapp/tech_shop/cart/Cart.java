package com.webapp.tech_shop.cart;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.webapp.tech_shop.shared.BaseEntity;
import com.webapp.tech_shop.user.User;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart extends BaseEntity {
    private Integer totalproduct;
    private BigDecimal totalPrice;
    @ManyToMany
    @JoinColumn(name = "customer_id")
    private User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
}
