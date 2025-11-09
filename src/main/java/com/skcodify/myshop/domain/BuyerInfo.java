package com.skcodify.myshop.domain;

import jakarta.persistence.Embeddable;

/**
 * Represents a snapshot of the buyer's information at the time of an order.
 * This class is designed to be embedded within the Order entity.
 */
@Embeddable
public class BuyerInfo {

    private long id;
    private String name;
    private String apartmentNumber;
    private String phone;

    /**
     * Default constructor for JPA.
     */
    public BuyerInfo() {
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}