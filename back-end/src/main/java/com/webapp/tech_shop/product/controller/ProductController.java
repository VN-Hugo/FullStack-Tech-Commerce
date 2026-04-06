package com.webapp.tech_shop.product.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.tech_shop.product.ProductService;
import com.webapp.tech_shop.product.model.Product;

import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final  ProductService productService;
    @GetMapping("/{id}")
        public Product getProduct(@PathVariable UUID id) {
            return productService.viewDetailsOfProduct(id);
        }
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }
}
