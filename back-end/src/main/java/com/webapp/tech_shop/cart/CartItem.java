package com.webapp.tech_shop.cart;

import java.time.LocalDateTime;


import org.springframework.data.annotation.CreatedDate;


import com.webapp.tech_shop.shared.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "cart_product")
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private UUID productId;
    private Integer quantity = 1;
    @CreatedDate
    private LocalDateTime createdAt;
}
