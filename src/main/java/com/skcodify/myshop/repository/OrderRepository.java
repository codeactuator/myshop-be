package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByBuyerId(Long userId);
    List<Order> findByDeliveryPartnerId(String deliveryPartnerId);
}