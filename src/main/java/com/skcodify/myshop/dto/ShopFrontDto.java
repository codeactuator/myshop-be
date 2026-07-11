package com.skcodify.myshop.dto;


import java.util.List;

public class ShopFrontDto {

    private Long userId;
    private String shopName;
    private String payeeName;
    private String bannerImageUrl;
    private String profileImageUrl;
    private String shopTagline;
    private String themeColor;
    private String upiId;
    private String gpayId;
    private String paytmId;
    private String phonepeId;
    private String paymentQrUrl;
    private List<String> socialMediaLinks;
    private boolean isVerified;
    private boolean isBlocked;
    private String phone;
    private String email;
    private List<ProductDto> popularProducts;

    // Getters and Setters

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProductDto> getPopularProducts() {
        return popularProducts;
    }

    public void setPopularProducts(List<ProductDto> popularProducts) {
        this.popularProducts = popularProducts;
    }
}