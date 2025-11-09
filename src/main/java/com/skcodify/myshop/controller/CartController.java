package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.CartDto;
import com.skcodify.myshop.dto.CartItemDto;
import com.skcodify.myshop.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartDto getCart(@RequestParam Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDto addToCart(@RequestParam Long userId, @RequestBody CartItemDto cartItemDto) {
        return cartService.addToCart(userId, cartItemDto.getProductId(), cartItemDto.getQuantity());
    }

    @DeleteMapping("/items/{cartItemId}")
    public CartDto removeFromCart(@RequestParam Long userId, @PathVariable Long cartItemId) {
        return cartService.removeFromCart(userId, cartItemId);
    }

    @DeleteMapping("/items")
    public CartDto removeProductFromCart(@RequestParam Long userId, @RequestParam String productId) {
        return cartService.removeProductFromCart(userId, productId);
    }
}