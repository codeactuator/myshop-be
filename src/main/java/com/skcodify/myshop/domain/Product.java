package com.skcodify.myshop.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents a product available for sale in the system.
 * This entity is mapped to the "products" table in the database.
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * The unique identifier for the product.
     * This is a string-based ID, so no auto-generation is used.
     */
    @Id
    private String id;

    /**
     * The name of the product.
     */
    @Column(nullable = false)
    private String name;

    /**
     * A detailed description of the product.
     */
    @Lob
    private String description;

    /**
     * The price of the product.
     * Using BigDecimal is best practice for monetary values.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * The category the product belongs to.
     */
    private String category;

    /**
     * The ID of the user who is selling this product.
     * This replaces the direct @ManyToOne relationship.
     */
    @Column(name = "seller_id")
    private Long userId;

    /**
     * The current stock quantity available.
     */
    private int stock;

    /**
     * The availability status of the product (e.g., "available", "sold_out").
     */
    private String status;

    /**
     * A list of URLs for the product images.
     */
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    /**
     * The date and time when the product was posted.
     */
    private ZonedDateTime postedDate;

    /**
     * Default constructor for JPA.
     */
    public Product() {
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ZonedDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(ZonedDateTime postedDate) {
        this.postedDate = postedDate;
    }
}