package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.Order;
import com.skcodify.myshop.domain.OrderItem;
import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.dto.OrderDto;
import com.skcodify.myshop.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setBuyerId(order.getBuyer().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setFulfillmentMethod(order.getFulfillmentMethod());
        dto.setPaymentMethod(order.getPaymentMethod());

        if (order.getDeliveryPartner() != null) {
            dto.setDeliveryPartnerId(order.getDeliveryPartner().getId());
        }

        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::toOrderItemDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private OrderItemDto toOrderItemDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        Product product = orderItem.getProduct();
        OrderItemDto dto = new OrderItemDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(orderItem.getPrice()); // Use price from order item for historical accuracy
        dto.setCategory(product.getCategory());
        dto.setImageUrls(product.getImageUrls());
        dto.setStatus(product.getStatus());
        dto.setPostedDate(product.getPostedDate());
        dto.setQuantity(orderItem.getQuantity());

        if (product.getSeller() != null) {
            dto.setUserId(String.valueOf(product.getSeller().getId()));
        }

        return dto;
    }
}