package com.webapp.tech_shop.product.model;



import java.util.List;

import com.webapp.tech_shop.shared.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.FetchType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="categories")
public class Category extends BaseEntity {
    private String name;
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (
        name = "product_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}
