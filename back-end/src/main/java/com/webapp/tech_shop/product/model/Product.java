package com.webapp.tech_shop.product.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.webapp.tech_shop.shared.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;
    private String name;
    private String pictureUrl;
    private Integer quantity;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ProductStatus status;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Category> categories;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
