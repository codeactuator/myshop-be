package com.skcodify.myshop.controller;

import com.skcodify.myshop.domain.Order;
import com.skcodify.myshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders(@RequestParam(required = false) Long userId,
                                 @RequestParam(required = false) String deliveryPartnerId) {
        return orderService.findOrders(userId, deliveryPartnerId);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        return orderService.findOrderById(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/{orderId}")
    public Order updateOrder(@PathVariable String orderId, @RequestBody Map<String, Object> updates) {
        return orderService.updateOrder(orderId, updates);
    }
}