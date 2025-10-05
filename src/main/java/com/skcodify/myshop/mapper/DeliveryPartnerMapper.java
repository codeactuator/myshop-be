package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.dto.DeliveryPartnerDto;
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
        dto.setLocation(partner.getLocation());

        if (partner.getUser() != null) {
            dto.setUserId(String.valueOf(partner.getUser().getId()));
        }

        if (partner.getVehicle() != null) {
            dto.setVehicleId(partner.getVehicle().getId());
        }

        return dto;
    }
}