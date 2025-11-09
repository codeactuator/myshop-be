package com.skcodify.myshop.domain;

import jakarta.persistence.*;

/**
 * Represents a delivery partner in the system.
 * This entity is mapped to the "delivery_partners" table in the database.
 */
@Entity
@Table(name = "delivery_partners")
public class DeliveryPartner {

    /**
     * The unique identifier for the delivery partner.
     * This is a string-based ID from the JSON, so no auto-generation is used.
     */
    @Id
    private String id;

    /**
     * The associated user account for this delivery partner.
     * This establishes a one-to-one relationship with the User entity.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The full name of the delivery partner.
     */
    private String name;

    /**
     * The phone number of the delivery partner.
     */
    private String phone;

    /**
     * The current availability status of the delivery partner.
     */
    private boolean isAvailable;

    /**
     * The number of deliveries currently active for this partner.
     */
    private int activeDeliveries;

    /**
     * The geographical location of the delivery partner.
     * This is an embedded object containing latitude and longitude.
     */
    @Embedded
    private Location location;

    /**
     * The vehicle assigned to this delivery partner.
     * This establishes a one-to-one relationship with the DeliveryVehicle entity.
     */
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private DeliveryVehicle vehicle;

    /**
     * Default constructor for JPA.
     */
    public DeliveryPartner() {
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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

    public DeliveryVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(DeliveryVehicle vehicle) {
        this.vehicle = vehicle;
    }
}