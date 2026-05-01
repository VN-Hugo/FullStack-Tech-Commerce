package com.webapp.tech_shop.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import  org.springframework.transaction.annotation.Transactional;
import com.webapp.tech_shop.exception.BaseException;
import com.webapp.tech_shop.exception.ErrorCode;
import com.webapp.tech_shop.product.model.Product;
import com.webapp.tech_shop.product.model.ProductStatus;
import com.webapp.tech_shop.shared.PageResponse;
import com.webapp.tech_shop.product.dto.ProductDetailResponse;
import com.webapp.tech_shop.product.dto.CreateProductRequest;
import com.webapp.tech_shop.product.dto.UpdateProductRequest;
import com.webapp.tech_shop.product.dto.ProductInfoForOrder;  
import com.webapp.tech_shop.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    

    public ProductDetailResponse viewDetailsOfProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDetailResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.fromCreateRequestToProduct(request);
        product.setStatus(ProductStatus.ACTIVE);
        product = productRepository.save(product);
        ProductDetailResponse detail = productMapper.toDto(product);
        return detail;
    }
    @Transactional
    public String deleteProduct(UUID id) {
        int check = productRepository.deleteProductById(id);
        if (check == 0) {
            throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return "Delete product successfully!";
    }
    @Transactional
    public ProductDetailResponse updateProduct(UpdateProductRequest request, UUID produdctId) {
        Product product = productRepository.findById(produdctId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProductFromUpdateRequest(request, product);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public PageResponse<ProductDetailResponse> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findWithPageReponseBy(pageable);
        return productMapper.toPageResponse(products);
    }

    public List<ProductInfoForOrder> getProductsForOrder(List<UUID> ids){
        List<Product> products = productRepository.findAllByIdIn(ids);
        return productMapper.toProductInfoForOrders(products);
    }

}

