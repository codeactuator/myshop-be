package com.skcodify.myshop.domain;

import jakarta.persistence.Embeddable;

/**
 * Represents a geographical location using latitude and longitude.
 * This class is designed to be embedded within other entities.
 */
@Embeddable
public class Location {

    /**
     * The latitude of the location.
     */
    private Double lat;

    /**
     * The longitude of the location.
     */
    private Double lng;

    /**
     * Default constructor for JPA.
     */
    public Location() {
    }

    // Getters and Setters

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}