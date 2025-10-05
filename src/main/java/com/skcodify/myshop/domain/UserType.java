package com.skcodify.myshop.domain;

/**
 * Defines the types of users in the system.
 */
public enum UserType {
    /** A user who can buy products. */
    BUYER,
    /** A user who can sell products. */
    SELLER,
    /** A user with administrative privileges. */
    ADMIN,
    /** A user who delivers orders. */
    DELIVERY_PARTNER
}