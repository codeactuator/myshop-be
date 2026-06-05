package com.skcodify.myshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skcodify.myshop.dto.CartDto;
import com.skcodify.myshop.dto.CartItemDto;
import com.skcodify.myshop.service.CartService;

@RestController
@RequestMapping("/carts")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addToCart(@RequestParam Long userId, @RequestBody CartItemDto cartItemDto) {
        return new ResponseEntity<>(cartService.addToCart(userId, cartItemDto.getProductId(), cartItemDto.getQuantity()), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> removeFromCart(@RequestParam Long userId, @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, cartItemId));
    }

    @DeleteMapping("/items")
    public ResponseEntity<CartDto> removeProductFromCart(@RequestParam Long userId, @RequestParam String productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }
}