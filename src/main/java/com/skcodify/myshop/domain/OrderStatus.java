package com.skcodify.myshop.domain;

/**
 * Defines the possible statuses of an order throughout its lifecycle.
 */
public enum OrderStatus {
    /** Initial state after order creation, before seller confirmation. */
    PENDING,
    /** Waiting for the buyer to complete the payment (for UPI/online payments). */
    AWAITING_PAYMENT,
    /** The order has been confirmed by the seller. */
    CONFIRMED,
    /** The seller is preparing the items for shipment. */
    PREPARING,
    /** The package is ready to be picked up by a delivery partner. */
    READY_FOR_SHIP,
    /** The order is currently with a delivery partner and on its way to the buyer. */
    OUT_FOR_DELIVERY,
    /** The order has been successfully delivered to the buyer. */
    DELIVERED
}