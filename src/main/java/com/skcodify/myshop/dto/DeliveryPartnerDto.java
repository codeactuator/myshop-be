package com.skcodify.myshop.dto;

import com.skcodify.myshop.domain.Location;

/**
 * Data Transfer Object for representing a Delivery Partner in API responses.
 */
public class DeliveryPartnerDto {

    private String id;
    private String userId;
    private String name;
    private String phone;
    private Boolean available;
    private int activeDeliveries;
    private Location location;
    private String vehicleId;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAvailable() {
        return available != null && available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getActiveDeliveries() {
        return activeDeliveries;
    }

    public void setActiveDeliveries(int activeDeliveries) {
        this.activeDeliveries = activeDeliveries;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}