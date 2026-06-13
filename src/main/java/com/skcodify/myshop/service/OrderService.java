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

    public List<OrderDto> findOrders(Long userId, String deliveryPartnerId, String status, String orderId) {
        List<Order> orders;
        if (orderId != null && !orderId.isEmpty()) {
            return orderRepository.findById(orderId).map(o -> List.of(orderMapper.toDto(o))).orElse(List.of());
        } else if (userId != null) {
            orders = orderRepository.findByBuyerId(userId);
        } else if (deliveryPartnerId != null) {
            orders = orderRepository.findByDeliveryPartnerId(deliveryPartnerId);
        } else if (status != null && !status.isEmpty()) {
            try {
                orders = orderRepository.findByStatus(OrderStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                orders = List.of(); // Return empty list for invalid status string
            }
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
        order.setId(UUID.randomUUID().toString().substring(0, 8)); // More robust ID generation
        order.setBuyer(buyer);
        order.setOrderDate(ZonedDateTime.now());
        order.setFulfillmentMethod(request.getFulfillmentMethod());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setUpiProvider(request.getUpiProvider());
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

        handleStatusUpdate(order, updates.getStatus());
        handlePartnerAssignment(order, updates.getDeliveryPartnerId());

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    private void handleStatusUpdate(Order order, OrderStatus newStatus) {
        if (newStatus == null || newStatus == order.getStatus()) {
            return;
        }

        // Side effect: If the order is marked as Delivered, free up the partner's slot
        if (newStatus == OrderStatus.DELIVERED && order.getDeliveryPartner() != null) {
            decrementPartnerLoad(order.getDeliveryPartner());
        }

        order.setStatus(newStatus);
    }

    private void handlePartnerAssignment(Order order, String newPartnerId) {
        if (newPartnerId == null) {
            return;
        }

        DeliveryPartner currentPartner = order.getDeliveryPartner();

        // Skip if the partner is already assigned to this order
        if (currentPartner != null && newPartnerId.equals(currentPartner.getId())) {
            return;
        }

        // 1. If re-assigning, decrement the load of the previous partner
        if (currentPartner != null && order.getStatus() != OrderStatus.DELIVERED) {
            decrementPartnerLoad(currentPartner);
        }

        // 2. Assign and increment the load of the new partner
        DeliveryPartner newPartner = deliveryPartnerRepository.findById(newPartnerId)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryPartner not found: " + newPartnerId));
        
        incrementPartnerLoad(newPartner);
        order.setDeliveryPartner(newPartner);

        // 3. Apply business heuristic for status transitions
        applyReadyForShipHeuristic(order);
    }

    private void incrementPartnerLoad(DeliveryPartner partner) {
        partner.setActiveDeliveries(partner.getActiveDeliveries() + 1);
        deliveryPartnerRepository.save(partner);
    }

    private void decrementPartnerLoad(DeliveryPartner partner) {
        partner.setActiveDeliveries(Math.max(0, partner.getActiveDeliveries() - 1));
        deliveryPartnerRepository.save(partner);
    }

    private void applyReadyForShipHeuristic(Order order) {
        // If a partner is assigned while the order is still in early stages, 
        // move it to READY_FOR_SHIP automatically.
        boolean isInEarlyStage = List.of(
                OrderStatus.PENDING,
                OrderStatus.AWAITING_PAYMENT,
                OrderStatus.CONFIRMED,
                OrderStatus.PREPARING
        ).contains(order.getStatus());

        if (isInEarlyStage) {
            order.setStatus(OrderStatus.READY_FOR_SHIP);
        }
    }
}