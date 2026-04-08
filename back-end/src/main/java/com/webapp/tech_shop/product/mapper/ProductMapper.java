package com.webapp.tech_shop.product.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.webapp.tech_shop.product.model.Product;
import com.webapp.tech_shop.shared.GenericMapper;
import com.webapp.tech_shop.product.dto.ProductDetailResponse;
import com.webapp.tech_shop.product.dto.CreateProductRequest;
import com.webapp.tech_shop.product.dto.UpdateProductRequest;
import com.webapp.tech_shop.product.dto.ProductInfoForOrder;

@Mapper(componentModel = "spring")

public interface ProductMapper extends GenericMapper<Product,  ProductDetailResponse> {
    
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product fromCreateRequestToProduct(CreateProductRequest request);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProductFromUpdateRequest(UpdateProductRequest request,@MappingTarget Product product);

    List<ProductInfoForOrder> toProductInfoForOrders(List<Product> products);

}
