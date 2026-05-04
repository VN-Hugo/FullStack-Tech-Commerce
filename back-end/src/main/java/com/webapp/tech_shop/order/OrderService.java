package com.webapp.tech_shop.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tech_shop.cart.Cart;
import com.webapp.tech_shop.cart.CartItem;
import com.webapp.tech_shop.cart.CartRepository;
import com.webapp.tech_shop.exception.BaseException;
import com.webapp.tech_shop.exception.ErrorCode;
import com.webapp.tech_shop.order.dto.CreateOrderRequest;
import com.webapp.tech_shop.order.model.Order;
import com.webapp.tech_shop.order.model.OrderItem;
import com.webapp.tech_shop.order.model.OrderStatus;
import com.webapp.tech_shop.product.ProductRepository;
import com.webapp.tech_shop.product.ProductService;
import com.webapp.tech_shop.product.dto.ProductInfoForOrder;
import com.webapp.tech_shop.product.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(UUID customerId, CreateOrderRequest request) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new BaseException(ErrorCode.CART_NOT_FOUND));

        if (cart.getCartItems().isEmpty()) {
            throw new BaseException(ErrorCode.CART_NOT_FOUND);
        }

        List<UUID> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .toList();

        List<ProductInfoForOrder> productInfos = productService.getProductsForOrder(productIds);
        Map<UUID, ProductInfoForOrder> productMap = productInfos.stream()
                .collect(Collectors.toMap(ProductInfoForOrder::id, p -> p));

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(item -> mapToOrderItem(item, productMap))
                .toList();

        BigDecimal total = calculateTotal(orderItems);

        Order order = Order.builder()
                .customerId(customerId)
                .status(OrderStatus.IN_PROCESS)
                .totalPrice(total)
                .addressShipping(request.addressShipping())
                .phoneNumber(request.phoneNumber())
                .customerName(request.customerName())
                .build();

        orderItems.forEach(i -> i.setOrder(order));
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        decrementProductStock(cart, productMap);
        clearCart(cart);

        return savedOrder;
    }

    public List<Order> getOrdersByCustomer(UUID customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    public Order getOrderById(UUID customerId, UUID orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));
    }

    private OrderItem mapToOrderItem(CartItem item, Map<UUID, ProductInfoForOrder> productMap) {
        ProductInfoForOrder productInfo = Optional.ofNullable(productMap.get(item.getProductId()))
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        if (productInfo.quantity() == null || item.getQuantity() > productInfo.quantity()) {
            throw new BaseException(ErrorCode.INSUFFICIENT_STOCK);
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(item.getProductId());
        orderItem.setProductName(productInfo.name());
        orderItem.setPrice(productInfo.price());
        orderItem.setQuantity(item.getQuantity());
        return orderItem;
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                    Integer quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void decrementProductStock(Cart cart, Map<UUID, ProductInfoForOrder> productMap) {
        List<UUID> productIds = cart.getCartItems().stream()
                .map(CartItem::getProductId)
                .toList();

        List<Product> products = productRepository.findAllByIdIn(productIds);
        Map<UUID, Product> productEntityMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        for (CartItem item : cart.getCartItems()) {
            Product product = productEntityMap.get(item.getProductId());
            if (product == null) {
                throw new BaseException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            int remaining = (product.getQuantity() != null ? product.getQuantity() : 0) - item.getQuantity();
            if (remaining < 0) {
                throw new BaseException(ErrorCode.INSUFFICIENT_STOCK);
            }
            product.setQuantity(remaining);
        }
        productRepository.saveAll(products);
    }

    private void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalProduct(0);
        cartRepository.save(cart);
    }
}
