package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.OrderDto;
import com.skcodify.myshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<OrderDto>> getOrders(@RequestParam(required = false) Long userId,
                                                   @RequestParam(required = false) String deliveryPartnerId,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(required = false) String orderId) {
        return ResponseEntity.ok(orderService.findOrders(userId, deliveryPartnerId, status, orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @RequestBody OrderDto updates) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, updates));
    }
}