package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.DeliveryVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryVehicleRepository extends JpaRepository<DeliveryVehicle, String> {
}