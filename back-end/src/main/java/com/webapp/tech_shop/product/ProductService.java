package com.webapp.tech_shop.product;



import org.springframework.stereotype.Service;


import com.webapp.tech_shop.product.model.Product;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public Product viewDetailsOfProduct(UUID id) {
       return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }
    public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
}

