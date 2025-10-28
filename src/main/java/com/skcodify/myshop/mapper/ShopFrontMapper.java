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
        dto.setBannerImageUrl(shopFront.getBannerImageUrl());
        dto.setProfileImageUrl(shopFront.getProfileImageUrl());
        dto.setShopTagline(shopFront.getShopTagline());
        dto.setThemeColor(shopFront.getThemeColor());
        dto.setSocialMediaLinks(shopFront.getSocialMediaLinks());

        return dto;
    }

    public void updateEntityFromDto(ShopFront shopFront, ShopFrontDto dto) {
        if (dto == null || shopFront == null) {
            return;
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
        if (dto.getSocialMediaLinks() != null) {
            shopFront.getSocialMediaLinks().clear();
            shopFront.getSocialMediaLinks().addAll(dto.getSocialMediaLinks());
        }
    }
}