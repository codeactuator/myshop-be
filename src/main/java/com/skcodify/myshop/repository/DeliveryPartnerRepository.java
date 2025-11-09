package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, String> {
    Optional<DeliveryPartner> findByUserId(Long userId);
}