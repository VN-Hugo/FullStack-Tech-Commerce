package com.webapp.tech_shop.product.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import com.webapp.tech_shop.product.ProductService;
import com.webapp.tech_shop.product.dto.CreateProductRequest;
import com.webapp.tech_shop.product.dto.ProductDetailResponse;
import com.webapp.tech_shop.product.dto.UpdateProductRequest;

import com.webapp.tech_shop.shared.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final  ProductService productService;
    // @GetMapping("/{id}")
    //     public Repo getProduct(@PathVariable UUID id) {
    //         return productService.viewDetailsOfProduct(id);
    //     }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> viewDetailsOfProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.viewDetailsOfProduct(id));

    }

    @PostMapping
    public ResponseEntity<ProductDetailResponse> createProduct(@RequestBody @Valid CreateProductRequest request){
        ProductDetailResponse detail = productService.createProduct(request);
        return ResponseEntity.ok(detail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id){
        return ResponseEntity.ok( productService.deleteProduct(id));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> updateProduct(
        @PathVariable UUID id,
        @RequestBody @Valid UpdateProductRequest request
    ){
        return ResponseEntity.ok(productService.updateProduct(request, id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductDetailResponse>> getProducts(
        @RequestParam(required = false, defaultValue = "1") int page
    ){
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by("createdAt").descending());
        return ResponseEntity.ok(productService.getProducts(pageable));
    }
}
