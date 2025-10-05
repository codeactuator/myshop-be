package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.dto.DeliveryVehicleDto;
import com.skcodify.myshop.service.DeliveryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/deliveryPartners")
    public List<DeliveryPartnerDto> getDeliveryPartners(@RequestParam(required = false) Long userId) {
        return deliveryService.findDeliveryPartners(userId);
    }

    @PatchMapping("/deliveryPartners/{partnerId}")
    public DeliveryPartnerDto updateDeliveryPartner(@PathVariable String partnerId, @RequestBody Map<String, Object> updates) {
        return deliveryService.updateDeliveryPartner(partnerId, updates);
    }

    @GetMapping("/deliveryVehicles/{vehicleId}")
    public DeliveryVehicleDto getDeliveryVehicle(@PathVariable String vehicleId) {
        return deliveryService.findDeliveryVehicleById(vehicleId);
    }
}