package com.webapp.tech_shop.cart.mapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.tech_shop.cart.Cart;
import com.webapp.tech_shop.cart.CartItem;
import com.webapp.tech_shop.cart.dto.CartItemResponse;
import com.webapp.tech_shop.cart.dto.CartResponse;
import com.webapp.tech_shop.product.dto.ProductInfoForOrder;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "cartId", target = "cartId")
    @Mapping(source = "cartItemResponses", target = "items")
    @Mapping(source = "totalProducts", target = "totalProducts")
    @Mapping(source = "totalPrice", target = "totalPrice")
    CartResponse toCartResponse(UUID cartId, List<CartItemResponse> cartItemResponses, 
                                Integer totalProducts, java.math.BigDecimal totalPrice);

    default CartItemResponse toCartItemResponse(CartItem cartItem, 
                                               Map<UUID, ProductInfoForOrder> productMap) {
        if (cartItem == null) {
            return null;
        }

        ProductInfoForOrder productInfo = productMap.get(cartItem.getProductId());
        java.math.BigDecimal price = (productInfo != null) ? productInfo.price() : java.math.BigDecimal.ZERO;
        String name = (productInfo != null) ? productInfo.name() : "Sản phẩm không tồn tại";
        java.math.BigDecimal totalPrice = price.multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProductId(),
                name,
                cartItem.getQuantity(),
                null,
                price,
                totalPrice
        );
    }

    default Cart toCart(UUID customerId) {
        if (customerId == null) {
            return null;
        }
        
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        cart.setTotalProduct(0);
        cart.setTotalPrice(java.math.BigDecimal.ZERO);
        return cart;
    }
}
