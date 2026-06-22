package com.skcodify.myshop.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the customizable shop front configuration for a seller.
 */
@Entity
@Table(name = "shop_fronts")
public class ShopFront {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String shopName;
    private String bannerImageUrl;
    private String profileImageUrl;
    private String shopTagline;
    private String themeColor;
    private String upiId;
    private String gpayId;
    private String paytmId;
    private String phonepeId;
    private String paymentQrUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shop_front_social_links", joinColumns = @JoinColumn(name = "shop_front_id"))
    @Column(name = "link")
    private List<String> socialMediaLinks = new ArrayList<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getGpayId() {
        return gpayId;
    }

    public void setGpayId(String gpayId) {
        this.gpayId = gpayId;
    }

    public String getPaytmId() {
        return paytmId;
    }

    public void setPaytmId(String paytmId) {
        this.paytmId = paytmId;
    }

    public String getPhonepeId() {
        return phonepeId;
    }

    public void setPhonepeId(String phonepeId) {
        this.phonepeId = phonepeId;
    }

    public String getPaymentQrUrl() {
        return paymentQrUrl;
    }

    public void setPaymentQrUrl(String paymentQrUrl) {
        this.paymentQrUrl = paymentQrUrl;
    }

    public List<String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(List<String> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }
}