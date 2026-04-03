package com.webapp.tech_shop.product.model;

import java.time.LocalDateTime;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.webapp.tech_shop.shared.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "brands")
public class Brand extends BaseEntity{
     private String name;
     private String description;
     private String logo_url;

    @CreatedDate
    private LocalDateTime createdAt;
}
