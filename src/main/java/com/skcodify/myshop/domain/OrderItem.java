package com.skcodify.myshop.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a single item within an order.
 * This entity links a product to an order and stores the quantity and price at the time of purchase.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    /**
     * The unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The order to which this item belongs.
     * This is the owning side of the one-to-many relationship with Order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * The product being ordered.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * The number of units of the product ordered.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * The price of a single unit of the product at the time the order was placed.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Default constructor for JPA.
     */
    public OrderItem() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}