package com.webapp.tech_shop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.webapp.tech_shop.product.model.Product;
import com.webapp.tech_shop.product.dto.ProductSearchCriteria;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductSearchCriteria criteria, Pageable pageable);
}
