package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.Order;
import com.skcodify.myshop.domain.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByBuyerId(Long userId);
    List<Order> findByDeliveryPartnerId(String deliveryPartnerId);
    // Add this method
    List<Order> findByStatus(OrderStatus status);
}