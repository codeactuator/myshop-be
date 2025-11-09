package com.skcodify.myshop.dto;

import com.skcodify.myshop.domain.BuyerInfo;
import com.skcodify.myshop.domain.FulfillmentMethod;
import com.skcodify.myshop.domain.OrderStatus;
import com.skcodify.myshop.domain.PaymentMethod;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Data Transfer Object for representing an Order in API responses.
 */
public class OrderDto {

    private String id;
    private BuyerInfo buyerInfo;
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;
    private ZonedDateTime orderDate;
    private OrderStatus status;
    private FulfillmentMethod fulfillmentMethod;
    private PaymentMethod paymentMethod;
    private String deliveryPartnerId;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BuyerInfo getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(BuyerInfo buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
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

    public String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(String deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }
}