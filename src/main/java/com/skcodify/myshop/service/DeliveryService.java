package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.domain.DeliveryVehicle;
import com.skcodify.myshop.repository.DeliveryPartnerRepository;
import com.skcodify.myshop.repository.DeliveryVehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService {

    private final DeliveryPartnerRepository partnerRepository;
    private final DeliveryVehicleRepository vehicleRepository;

    public DeliveryService(DeliveryPartnerRepository partnerRepository, DeliveryVehicleRepository vehicleRepository) {
        this.partnerRepository = partnerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<DeliveryPartner> findDeliveryPartners(Long userId) {
        if (userId != null) {
            return partnerRepository.findByUserId(userId)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }
        return partnerRepository.findAll();
    }

    public DeliveryVehicle findDeliveryVehicleById(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryVehicle not found with id: " + vehicleId));
    }

    @Transactional
    public DeliveryPartner updateDeliveryPartner(String partnerId, Map<String, Object> updates) {
        DeliveryPartner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryPartner not found with id: " + partnerId));

        if (updates.containsKey("isAvailable")) {
            partner.setAvailable((Boolean) updates.get("isAvailable"));
        }

        return partnerRepository.save(partner);
    }
}