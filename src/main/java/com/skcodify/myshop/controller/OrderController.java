package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.OrderDto;
import com.skcodify.myshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDto> getOrders(@RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) String deliveryPartnerId) {
        return orderService.findOrders(userId, deliveryPartnerId);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable String orderId) {
        return orderService.findOrderById(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @PatchMapping("/{orderId}")
    public OrderDto updateOrder(@PathVariable String orderId, @RequestBody OrderDto updates) {
        return orderService.updateOrder(orderId, updates);
    }
}