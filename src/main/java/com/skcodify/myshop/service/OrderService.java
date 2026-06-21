package com.skcodify.myshop.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.skcodify.myshop.controller.NotificationController;
import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.domain.Order;
import com.skcodify.myshop.domain.OrderItem;
import com.skcodify.myshop.domain.OrderStatus;
import com.skcodify.myshop.domain.PaymentMethod;
import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.domain.UserType;
import com.skcodify.myshop.dto.OrderDto;
import com.skcodify.myshop.mapper.OrderMapper;
import com.skcodify.myshop.repository.DeliveryPartnerRepository;
import com.skcodify.myshop.repository.OrderRepository;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

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

        // Notify Sellers of the items in this order
        if (savedOrder.getItems() != null) {
            triggerAfterCommit(() -> {
                savedOrder.getItems().stream()
                    .map(item -> item.getProduct().getUserId())
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .forEach(sellerId -> NotificationController.sendNotification(
                        String.valueOf(sellerId),
                        "alert",
                        "New order #" + savedOrder.getId() + " received!"
                    ));

                // Notify Admins
                userRepository.findAll().stream()
                    .filter(u -> u.getUserType() == UserType.ADMIN)
                    .forEach(admin -> NotificationController.sendNotification(
                        String.valueOf(admin.getId()),
                        "alert",
                        "New order #" + savedOrder.getId() + " has been placed!"
                    ));
            });
        }

        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrder(String orderId, OrderDto updates) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        handleStatusUpdate(order, updates.getStatus());
        handlePartnerAssignment(order, updates.getDeliveryPartnerId());

        Order updatedOrder = orderRepository.save(order);

        // Notify Buyer of status update
        if (updatedOrder.getBuyer() != null) {
            String buyerMessage;
            if (updatedOrder.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
                buyerMessage = "Your order #" + updatedOrder.getId() + " is out for delivery!";
            } else if (updatedOrder.getStatus() == OrderStatus.DELIVERED) {
                buyerMessage = "Your order #" + updatedOrder.getId() + " has been delivered. Enjoy!";
            } else {
                buyerMessage = "Your order #" + updatedOrder.getId() + " status is now " + updatedOrder.getStatus().name().replace('_', ' ');
            }
            NotificationController.sendNotification(
                String.valueOf(updatedOrder.getBuyer().getId()),
                "alert",
                buyerMessage
            );
        }

        // Notify Delivery Partner if assigned
        if (updatedOrder.getDeliveryPartner() != null && updatedOrder.getDeliveryPartner().getUser() != null) {
            String partnerMessage;
            if (updates.getDeliveryPartnerId() != null) {
                partnerMessage = "A new delivery has been assigned to you: Order #" + updatedOrder.getId() + "!";
            } else {
                partnerMessage = "Delivery task for Order #" + updatedOrder.getId() + " is now " + updatedOrder.getStatus().name().replace('_', ' ');
            }
            NotificationController.sendNotification(
                String.valueOf(updatedOrder.getDeliveryPartner().getUser().getId()),
                "alert",
                partnerMessage
            );
        }

        // Notify Sellers of status update
        if (updatedOrder.getItems() != null) {
            String sellerMessage;
            if (updatedOrder.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
                sellerMessage = "Order #" + updatedOrder.getId() + " has been picked up by the delivery partner.";
            } else if (updatedOrder.getStatus() == OrderStatus.DELIVERED) {
                sellerMessage = "Order #" + updatedOrder.getId() + " has been successfully delivered.";
            } else {
                sellerMessage = "Order #" + updatedOrder.getId() + " status updated to " + updatedOrder.getStatus().name().replace('_', ' ');
            }
            updatedOrder.getItems().stream()
                .map(item -> item.getProduct().getUserId())
                .filter(java.util.Objects::nonNull)
                .distinct()
                .forEach(sellerId -> NotificationController.sendNotification(
                    String.valueOf(sellerId),
                    "alert",
                    sellerMessage
                ));
        }

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

    private void triggerAfterCommit(Runnable runnable) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }
}