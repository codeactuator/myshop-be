package com.skcodify.myshop.domain;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * Represents a user review for a product.
 * This entity is mapped to the "reviews" table in the database.
 */
@Entity
@Table(name = "reviews")
public class Review {

    /**
     * The unique identifier for the review.
     * This is a string-based ID from the JSON, so no auto-generation is used.
     */
    @Id
    private String id;

    /**
     * The product that is being reviewed.
     * This is a many-to-one relationship, as a product can have multiple reviews.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * The user who wrote the review.
     * This is a many-to-one relationship, as a user can write multiple reviews.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The rating given by the user, typically on a scale (e.g., 1-5).
     */
    @Column(nullable = false)
    private int rating;

    /**
     * The text comment left by the user.
     * Using @Lob is a good practice for fields that might contain long text.
     */
    @Lob
    private String comment;

    /**
     * The date and time when the review was submitted.
     */
    @Column(nullable = false)
    private ZonedDateTime reviewDate;

    /**
     * Default constructor for JPA.
     */
    public Review() {
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(ZonedDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}