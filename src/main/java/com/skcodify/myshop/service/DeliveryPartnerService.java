package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.DeliveryPartner;
import com.skcodify.myshop.domain.Location;
import com.skcodify.myshop.dto.DeliveryPartnerDto;
import com.skcodify.myshop.dto.LocationDto;
import com.skcodify.myshop.mapper.DeliveryPartnerMapper;
import com.skcodify.myshop.repository.DeliveryPartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final DeliveryPartnerMapper deliveryPartnerMapper;

    public DeliveryPartnerService(DeliveryPartnerRepository deliveryPartnerRepository, DeliveryPartnerMapper deliveryPartnerMapper) {
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.deliveryPartnerMapper = deliveryPartnerMapper;
    }

    public List<DeliveryPartnerDto> findPartners(Long userId) {
        // If your system links partners to users, filter here; otherwise, return all
        return deliveryPartnerRepository.findAll().stream()
                .map(deliveryPartnerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeliveryPartnerDto createPartner(DeliveryPartnerDto partnerDto) {
        DeliveryPartner partner = new DeliveryPartner();
        // Generate a unique ID similar to your product pattern
        partner.setId("partner-" + UUID.randomUUID().toString().substring(0, 5));
        partner.setName(partnerDto.getName());
        partner.setPhone(partnerDto.getPhone());
        partner.setAvailable(partnerDto.getAvailable() != null ? partnerDto.getAvailable() : true);
        
        if (partnerDto.getLocation() != null) {
            Location loc = new Location();
            loc.setLat(partnerDto.getLocation().getLat());
            loc.setLng(partnerDto.getLocation().getLng());
            partner.setLocation(loc);
        }

        return deliveryPartnerMapper.toDto(deliveryPartnerRepository.save(partner));
    }

    @Transactional
    public DeliveryPartnerDto updatePartner(String id, DeliveryPartnerDto updates) {
        DeliveryPartner partner = deliveryPartnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));

        if (updates.getName() != null) {
            partner.setName(updates.getName());
        }
        if (updates.getPhone() != null) {
            partner.setPhone(updates.getPhone());
        }
        if (updates.getAvailable() != null) {
            partner.setAvailable(updates.getAvailable());
        }

        return deliveryPartnerMapper.toDto(deliveryPartnerRepository.save(partner));
    }

    public DeliveryPartnerDto findPartnerById(String id) {
        return deliveryPartnerRepository.findById(id)
                .map(deliveryPartnerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));
    }
}