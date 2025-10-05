package com.skcodify.myshop.dto;

/**
 * Data Transfer Object for representing a Delivery Vehicle in API responses.
 */
public class DeliveryVehicleDto {

    private String id;
    private String vehicleNumber;
    private String vehicleType;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}