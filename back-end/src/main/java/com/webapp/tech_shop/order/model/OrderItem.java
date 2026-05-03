package com.webapp.tech_shop.order.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import com.webapp.tech_shop.shared.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_detail")
public class OrderItem extends BaseEntity {
    private String productName;
    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
}
