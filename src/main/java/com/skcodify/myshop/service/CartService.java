package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.Cart;
import com.skcodify.myshop.domain.CartItem;
import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.CartDto;
import com.skcodify.myshop.dto.CartItemDto;
import com.skcodify.myshop.mapper.CartMapper;
import com.skcodify.myshop.repository.CartRepository;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional
    public CartDto getCartByUserId(Long userId) {
        Cart cart = findOrCreateCartByUserId(userId);
        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDto addToCart(Long userId, String productId, int quantity) {
        Cart cart = findOrCreateCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        // Check if the product is already in the cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity if product already exists
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item to cart
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.addCartItem(newItem);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto removeFromCart(Long userId, Long cartItemId) {
        Cart cart = findOrCreateCartByUserId(userId);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with id: " + cartItemId));

        cart.removeCartItem(itemToRemove);

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto removeProductFromCart(Long userId, String productId) {
        Cart cart = findOrCreateCartByUserId(userId);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " not found in cart."));

        cart.removeCartItem(itemToRemove);

        return cartMapper.toDto(cartRepository.save(cart));
    }

    private Cart findOrCreateCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
}