package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.ShopFront;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopFrontRepository extends JpaRepository<ShopFront, Long> {

    Optional<ShopFront> findByUserId(Long userId);
}