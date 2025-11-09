package com.skcodify.myshop.domain;

import jakarta.persistence.*;

/**
 * Represents a delivery vehicle in the system.
 * This entity is mapped to the "delivery_vehicles" table in the database.
 */
@Entity
@Table(name = "delivery_vehicles")
public class DeliveryVehicle {

    /**
     * The unique identifier for the delivery vehicle.
     * In the provided JSON, this is a string, so we are not using auto-generation.
     */
    @Id
    private String id;

    /**
     * The registration number of the vehicle. It must be unique and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String vehicleNumber;

    /**
     * The type of the vehicle (e.g., BIKE, TRUCK).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    /**
     * Default constructor for JPA.
     */
    public DeliveryVehicle() {
    }

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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}