package com.skcodify.myshop.dto;

/**
 * Data Transfer Object for Delivery Partner operations.
 */
public class DeliveryPartnerDto {
    private String id;
    private String name;
    private String phone;
    private Boolean available;
    private LocationDto location;
    private int activeDeliveries;
    private String userId;
    private String vehicleId;

    public DeliveryPartnerDto() {}

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public int getActiveDeliveries() {
        return activeDeliveries;
    }

    public void setActiveDeliveries(int activeDeliveries) {
        this.activeDeliveries = activeDeliveries;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}