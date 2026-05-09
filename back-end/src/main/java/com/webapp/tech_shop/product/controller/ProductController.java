package com.webapp.tech_shop.product.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import com.webapp.tech_shop.product.ProductService;
import com.webapp.tech_shop.product.dto.ProductSearchCriteria;
import com.webapp.tech_shop.product.dto.CreateProductRequest;
import com.webapp.tech_shop.product.dto.ProductDetailResponse;
import com.webapp.tech_shop.product.dto.UpdateProductRequest;

import com.webapp.tech_shop.shared.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    private final  ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Get product details", description = "Retrieve detailed information about a specific product")
    public ResponseEntity<ProductDetailResponse> viewDetailsOfProduct(
        @Parameter(description = "Product ID", required = true, schema = @Schema(type = "string", format = "uuid")) @PathVariable UUID id) {
        return ResponseEntity.ok(productService.viewDetailsOfProduct(id));

    }

    @PostMapping
    public ResponseEntity<ProductDetailResponse> createProduct(@RequestBody @Valid CreateProductRequest request){
        ProductDetailResponse detail = productService.createProduct(request);
        return ResponseEntity.ok(detail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    public ResponseEntity<String> deleteProduct(
        @Parameter(description = "Product ID", required = true, schema = @Schema(type = "string", format = "uuid")) @PathVariable UUID id){
        return ResponseEntity.ok( productService.deleteProduct(id));
    }
    
    @PatchMapping("/{id}")
    @Operation(summary = "Update product", description = "Update product information by its ID")
    public ResponseEntity<ProductDetailResponse> updateProduct(
        @Parameter(description = "Product ID", required = true, schema = @Schema(type = "string", format = "uuid")) @PathVariable UUID id,
        @RequestBody @Valid UpdateProductRequest request
    ){
        return ResponseEntity.ok(productService.updateProduct(request, id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductDetailResponse>> getProducts(
        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
        @RequestParam(name = "size", required = false, defaultValue = "10") int size,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "priceMin", required = false) BigDecimal priceMin,
        @RequestParam(name = "priceMax", required = false) BigDecimal priceMax,
        @RequestParam(name = "brandId", required = false) UUID brandId,
        @RequestParam(name = "brandName", required = false) String brandName,
        @RequestParam(name = "categoryId", required = false) UUID categoryId,
        @RequestParam(name = "categoryName", required = false) String categoryName
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        ProductSearchCriteria criteria = new ProductSearchCriteria(name, priceMin, priceMax, brandId, brandName, categoryId, categoryName);
        return ResponseEntity.ok(productService.searchProducts(criteria, pageable));
    }
}