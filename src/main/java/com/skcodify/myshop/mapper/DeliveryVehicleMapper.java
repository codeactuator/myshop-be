package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.DeliveryVehicle;
import com.skcodify.myshop.dto.DeliveryVehicleDto;
import org.springframework.stereotype.Component;

@Component
public class DeliveryVehicleMapper {

    public DeliveryVehicleDto toDto(DeliveryVehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        DeliveryVehicleDto dto = new DeliveryVehicleDto();
        dto.setId(vehicle.getId());
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        if (vehicle.getVehicleType() != null) {
            dto.setVehicleType(vehicle.getVehicleType().name().toLowerCase());
        }
        return dto;
    }
}