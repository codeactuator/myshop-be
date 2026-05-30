package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.dto.DeliveryVehicleDto;
import com.skcodify.myshop.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/partners")
    public ResponseEntity<List<DeliveryPartnerDto>> getDeliveryPartners(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(deliveryService.findDeliveryPartners(userId));
    }

    @PatchMapping("/partners/{partnerId}")
    public ResponseEntity<DeliveryPartnerDto> updateDeliveryPartner(@PathVariable String partnerId, @RequestBody DeliveryPartnerDto updates) { // NOSONAR
        return ResponseEntity.ok(deliveryService.updateDeliveryPartner(partnerId, updates));
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<DeliveryVehicleDto> getDeliveryVehicle(@PathVariable String vehicleId) {
        return ResponseEntity.ok(deliveryService.findDeliveryVehicleById(vehicleId));
    }
}