package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.*;

import com.skcodify.myshop.dto.OrderDto;

import com.skcodify.myshop.mapper.OrderMapper;
import com.skcodify.myshop.repository.DeliveryPartnerRepository;
import com.skcodify.myshop.repository.OrderRepository;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, DeliveryPartnerRepository deliveryPartnerRepository, UserRepository userRepository, ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> findOrders(Long userId, String deliveryPartnerId) {
        List<Order> orders;
        if (userId != null) {
            orders = orderRepository.findByBuyerId(userId);
        } else if (deliveryPartnerId != null) {
            orders = orderRepository.findByDeliveryPartnerId(deliveryPartnerId);
        } else {
            orders = orderRepository.findAll();
        }
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public OrderDto findOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDto createOrder(OrderDto request) {
        User buyer = userRepository.findById(request.getBuyerInfo().getId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found with phone: " + request.getBuyerInfo().getPhone()));

        Order order = new Order();
        order.setId(UUID.randomUUID().toString().substring(0, 4)); // Simple ID generation
        order.setBuyer(buyer);
        order.setOrderDate(ZonedDateTime.now());
        order.setFulfillmentMethod(request.getFulfillmentMethod());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(request.getPaymentMethod() == PaymentMethod.COD ? OrderStatus.PENDING : OrderStatus.AWAITING_PAYMENT);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemRequest.getId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrder(String orderId, OrderDto updates) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (updates.getStatus() != null) {
            order.setStatus(updates.getStatus());
        }
        if (updates.getDeliveryPartnerId() != null) {
            DeliveryPartner partner = deliveryPartnerRepository.findById(updates.getDeliveryPartnerId())
                    .orElseThrow(() -> new EntityNotFoundException("DeliveryPartner not found with id: " + updates.getDeliveryPartnerId()));
            order.setDeliveryPartner(partner);
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }
}