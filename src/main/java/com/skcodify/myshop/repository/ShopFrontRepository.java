package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.ShopFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopFrontRepository extends JpaRepository<ShopFront, Long> {

    Optional<ShopFront> findByUserId(Long userId);

    List<ShopFront> findByUserIdIn(List<Long> userIds);
}