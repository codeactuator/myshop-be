package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPartnerMapper {

    public DeliveryPartnerDto toDto(DeliveryPartner partner) {
        if (partner == null) {
            return null;
        }

        DeliveryPartnerDto dto = new DeliveryPartnerDto();
        dto.setId(partner.getId());
        dto.setName(partner.getName());
        dto.setPhone(partner.getPhone());
        dto.setAvailable(partner.isAvailable());
        dto.setActiveDeliveries(partner.getActiveDeliveries());

        if (partner.getLocation() != null) {
            dto.setLocation(new LocationDto(partner.getLocation().getLat(), partner.getLocation().getLng()));
        }

        if (partner.getUser() != null) {
            dto.setUserId(String.valueOf(partner.getUser().getId()));
        }

        if (partner.getVehicle() != null) {
            dto.setVehicleId(partner.getVehicle().getId());
        }

        return dto;
    }
}