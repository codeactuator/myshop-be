package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.dto.DeliveryVehicleDto;
import com.skcodify.myshop.service.DeliveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
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
    public DeliveryPartnerDto updateDeliveryPartner(@PathVariable String partnerId, @RequestBody DeliveryPartnerDto updates) { // NOSONAR
        return deliveryService.updateDeliveryPartner(partnerId, updates);
    }

    @GetMapping("/deliveryVehicles/{vehicleId}")
    public DeliveryVehicleDto getDeliveryVehicle(@PathVariable String vehicleId) {
        return deliveryService.findDeliveryVehicleById(vehicleId);
    }
}