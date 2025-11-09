package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.domain.DeliveryVehicle;
import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.dto.DeliveryVehicleDto;
import com.skcodify.myshop.mapper.DeliveryPartnerMapper;
import com.skcodify.myshop.mapper.DeliveryVehicleMapper;
import com.skcodify.myshop.repository.DeliveryPartnerRepository;
import com.skcodify.myshop.repository.DeliveryVehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    private final DeliveryPartnerRepository partnerRepository;
    private final DeliveryVehicleRepository vehicleRepository;
    private final DeliveryPartnerMapper partnerMapper;
    private final DeliveryVehicleMapper vehicleMapper;

    public DeliveryService(DeliveryPartnerRepository partnerRepository, DeliveryVehicleRepository vehicleRepository, DeliveryPartnerMapper partnerMapper, DeliveryVehicleMapper vehicleMapper) {
        this.partnerRepository = partnerRepository;
        this.vehicleRepository = vehicleRepository;
        this.partnerMapper = partnerMapper;
        this.vehicleMapper = vehicleMapper;
    }

    public List<DeliveryPartnerDto> findDeliveryPartners(Long userId) {
        List<DeliveryPartner> partners;
        if (userId != null) {
            partners = partnerRepository.findByUserId(userId).map(List::of).orElse(List.of());
        } else {
            partners = partnerRepository.findAll();
        }
        return partners.stream().map(partnerMapper::toDto).collect(Collectors.toList());
    }

    public DeliveryVehicleDto findDeliveryVehicleById(String vehicleId) {
        DeliveryVehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryVehicle not found with id: " + vehicleId));
        return vehicleMapper.toDto(vehicle);
    }

    @Transactional
    public DeliveryPartnerDto updateDeliveryPartner(String partnerId, DeliveryPartnerDto updates) {
        DeliveryPartner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryPartner not found with id: " + partnerId));

        if (updates.isAvailable() != partner.isAvailable()) { // Check if the value is different
            partner.setAvailable(updates.isAvailable());
        }

        return partnerMapper.toDto(partnerRepository.save(partner));
    }
}