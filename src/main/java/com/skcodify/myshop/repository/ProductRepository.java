package com.skcodify.myshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skcodify.myshop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByStatus(String status);

    List<Product> findByUserId(Long userId);

    @Query("SELECT p FROM Product p JOIN User u ON p.userId = u.id JOIN u.serviceSocieties s WHERE s.id = :societyId")
    List<Product> findBySocietyId(@Param("societyId") Long societyId);

    @Query("SELECT p FROM Product p JOIN User u ON p.userId = u.id JOIN u.serviceSocieties s WHERE p.status = :status AND s.id = :societyId")
    List<Product> findByStatusAndSocietyId(@Param("status") String status, @Param("societyId") Long societyId);

}