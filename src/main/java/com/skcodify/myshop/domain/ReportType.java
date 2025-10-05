package com.skcodify.myshop.domain;

/**
 * Defines the types of reports that can be generated in the system.
 */
public enum ReportType {
    /** A summary of sales within a given period. */
    SALES_SUMMARY,
    /** A report on the current status of product inventory. */
    INVENTORY_STATUS,
    /** A report on the performance and metrics of delivery partners. */
    DELIVERY_PERFORMANCE,
    /** A report on user activity, such as registrations and orders. */
    USER_ACTIVITY
}