package com.skcodify.myshop.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Data Transfer Object for representing an item within an order.
 */
public class OrderItemDto {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private List<String> imageUrls;
    private String userId;
    private String status;
    private ZonedDateTime postedDate;
    private int quantity;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(ZonedDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}