package com.webapp.tech_shop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.webapp.tech_shop.product.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Modifying
    @Query("Delete from Product p where p.id = :id")
    int deleteProductById(@Param("id") UUID id);

    Page<Product> findWithPageReponseBy(Pageable pageable);

    List<Product> findAllByIdIn(List<UUID> ids);
}

