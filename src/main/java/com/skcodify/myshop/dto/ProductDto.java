package com.skcodify.myshop.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Data Transfer Object for representing a Product in API responses.
 */
public class ProductDto {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private List<String> imageUrls;
    private String userId;
    private String status;
    private ZonedDateTime postedDate;
    private Integer stock;
    private Set<SocietyDto> serviceSocieties;
    private String sellerName;
    private String shopName;
    private String shopTagline;
    private boolean isVerifiedSeller;
    private String sellerProfileImageUrl;

    // Getters and Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(ZonedDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Set<SocietyDto> getServiceSocieties() {
        return serviceSocieties;
    }

    public void setServiceSocieties(Set<SocietyDto> serviceSocieties) {
        this.serviceSocieties = serviceSocieties;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
    }

    public boolean isVerifiedSeller() {
        return isVerifiedSeller;
    }

    public void setVerifiedSeller(boolean verifiedSeller) {
        isVerifiedSeller = verifiedSeller;
    }

    public String getSellerProfileImageUrl() {
        return sellerProfileImageUrl;
    }

    public void setSellerProfileImageUrl(String sellerProfileImageUrl) {
        this.sellerProfileImageUrl = sellerProfileImageUrl;
    }
}