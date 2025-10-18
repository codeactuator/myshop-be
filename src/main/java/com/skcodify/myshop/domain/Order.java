package com.skcodify.myshop.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents a customer order in the system.
 * This entity is mapped to the "orders" table in the database.
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * The unique identifier for the order.
     * This is a string-based ID from the JSON, so no auto-generation is used.
     */
    @Id
    private String id;

    /**
     * The user who placed the order (the buyer).
     * This is a many-to-one relationship, as a user can have multiple orders.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    /**
     * The list of items included in this order.
     * This is a one-to-many relationship, managed by the OrderItem entity.
     * CascadeType.ALL ensures that order items are saved/deleted along with the order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    /**
     * The total cost of the order.
     * Using BigDecimal is recommended for financial calculations to avoid floating-point errors.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * The date and time when the order was placed.
     */
    private ZonedDateTime orderDate;

    /**
     * The current status of the order (e.g., PENDING, CONFIRMED, DELIVERED).
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * The method of fulfillment for the order (e.g., DELIVERY).
     */
    @Enumerated(EnumType.STRING)
    private FulfillmentMethod fulfillmentMethod;

    /**
     * The method of payment for the order (e.g., UPI, COD).
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * The delivery partner assigned to this order.
     * This is a many-to-one relationship, as a partner can handle multiple orders.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_partner_id")
    private DeliveryPartner deliveryPartner;

    /**
     * Default constructor for JPA.
     */
    public Order() {
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public FulfillmentMethod getFulfillmentMethod() {
        return fulfillmentMethod;
    }

    public void setFulfillmentMethod(FulfillmentMethod fulfillmentMethod) {
        this.fulfillmentMethod = fulfillmentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }
}