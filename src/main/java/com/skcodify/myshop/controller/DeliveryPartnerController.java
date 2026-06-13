package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.service.DeliveryPartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery/partners")
public class DeliveryPartnerController {

    private final DeliveryPartnerService deliveryPartnerService;

    public DeliveryPartnerController(DeliveryPartnerService deliveryPartnerService) {
        this.deliveryPartnerService = deliveryPartnerService;
    }

    @PostMapping
    public ResponseEntity<DeliveryPartnerDto> createPartner(@RequestBody DeliveryPartnerDto partnerDto) {
        return new ResponseEntity<>(deliveryPartnerService.createPartner(partnerDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeliveryPartnerDto> updatePartner(@PathVariable String id, @RequestBody DeliveryPartnerDto updates) {
        return ResponseEntity.ok(deliveryPartnerService.updatePartner(id, updates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryPartnerDto> getPartnerById(@PathVariable String id) {
        return ResponseEntity.ok(deliveryPartnerService.findPartnerById(id));
    }
}