package com.skcodify.myshop.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for representing an item within a cart.
 */
public class CartItemDto {

    private Long id; // This will be the CartItem ID
    private String productId;
    private String productName;
    private BigDecimal price;
    private String imageUrl; // A single representative image
    private int quantity;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) { // This will be the price per unit
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) { // This will be the quantity of this item in the cart
        this.quantity = quantity;
    }
}