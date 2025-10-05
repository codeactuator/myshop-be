package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.domain.Order;
import com.skcodify.myshop.domain.OrderStatus;
import com.skcodify.myshop.repository.DeliveryPartnerRepository;
import com.skcodify.myshop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;

    public OrderService(OrderRepository orderRepository, DeliveryPartnerRepository deliveryPartnerRepository) {
        this.orderRepository = orderRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
    }

    public List<Order> findOrders(Long userId, String deliveryPartnerId) {
        if (userId != null) {
            return orderRepository.findByBuyerId(userId);
        }
        if (deliveryPartnerId != null) {
            return orderRepository.findByDeliveryPartnerId(deliveryPartnerId);
        }
        return orderRepository.findAll();
    }

    public Order findOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    public Order createOrder(Order order) {
        // Add any order creation logic here (e.g., setting default status, date)
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(String orderId, Map<String, Object> updates) {
        Order order = findOrderById(orderId);

        updates.forEach((key, value) -> {
            if ("status".equals(key)) {
                order.setStatus(OrderStatus.valueOf((String) value));
            }
            if ("deliveryPartnerId".equals(key)) {
                DeliveryPartner partner = deliveryPartnerRepository.findById((String) value)
                        .orElseThrow(() -> new EntityNotFoundException("DeliveryPartner not found with id: " + value));
                order.setDeliveryPartner(partner);
            }
        });

        return orderRepository.save(order);
    }
}