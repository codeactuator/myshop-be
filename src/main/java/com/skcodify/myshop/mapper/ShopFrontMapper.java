package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.ShopFront;
import com.skcodify.myshop.dto.ShopFrontDto;
import org.springframework.stereotype.Component;

@Component
public class ShopFrontMapper {

    public ShopFrontDto toDto(ShopFront shopFront) {
        if (shopFront == null) {
            return null;
        }

        ShopFrontDto dto = new ShopFrontDto();
        dto.setUserId(shopFront.getUser().getId());
        dto.setShopName(shopFront.getShopName());
        dto.setBannerImageUrl(shopFront.getBannerImageUrl());
        dto.setProfileImageUrl(shopFront.getProfileImageUrl());
        dto.setShopTagline(shopFront.getShopTagline());
        dto.setThemeColor(shopFront.getThemeColor());
        dto.setUpiId(shopFront.getUpiId());
        dto.setGpayId(shopFront.getGpayId());
        dto.setPaytmId(shopFront.getPaytmId());
        dto.setPhonepeId(shopFront.getPhonepeId());
        dto.setPaymentQrUrl(shopFront.getPaymentQrUrl());
        dto.setSocialMediaLinks(shopFront.getSocialMediaLinks());

        return dto;
    }

    public void updateEntityFromDto(ShopFront shopFront, ShopFrontDto dto) {
        if (dto == null || shopFront == null) {
            return;
        }

        if (dto.getShopName() != null) {
            shopFront.setShopName(dto.getShopName());
        }
        if (dto.getBannerImageUrl() != null) {
            shopFront.setBannerImageUrl(dto.getBannerImageUrl());
        }
        if (dto.getProfileImageUrl() != null) {
            shopFront.setProfileImageUrl(dto.getProfileImageUrl());
        }
        if (dto.getShopTagline() != null) {
            shopFront.setShopTagline(dto.getShopTagline());
        }
        if (dto.getThemeColor() != null) {
            shopFront.setThemeColor(dto.getThemeColor());
        }
        if (dto.getUpiId() != null) {
            shopFront.setUpiId(dto.getUpiId());
        }
        if (dto.getGpayId() != null) {
            shopFront.setGpayId(dto.getGpayId());
        }
        if (dto.getPaytmId() != null) {
            shopFront.setPaytmId(dto.getPaytmId());
        }
        if (dto.getPhonepeId() != null) {
            shopFront.setPhonepeId(dto.getPhonepeId());
        }
        if (dto.getPaymentQrUrl() != null) {
            shopFront.setPaymentQrUrl(dto.getPaymentQrUrl());
        }
        if (dto.getSocialMediaLinks() != null) {
            shopFront.getSocialMediaLinks().clear();
            shopFront.getSocialMediaLinks().addAll(dto.getSocialMediaLinks());
        }
    }
}