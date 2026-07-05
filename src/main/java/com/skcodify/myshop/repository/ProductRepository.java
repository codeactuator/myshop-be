package com.skcodify.myshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skcodify.myshop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByStatus(String status);

    List<Product> findBySellerId(Long sellerId);

    @Query(value = "SELECT * FROM products WHERE seller_id IN (" +
                   "SELECT user_id FROM user_societies WHERE society_id = :societyId" +
                   ")", nativeQuery = true)
    List<Product> findBySocietyId(@Param("societyId") Long societyId);

    @Query("SELECT p FROM Product p " +
           "JOIN p.seller s " +
           "JOIN s.serviceSocieties soc " +
           "WHERE p.status = :status AND soc.id = :societyId")
    List<Product> findByStatusAndSocietyId(
        @Param("status") String status, 
        @Param("societyId") Long societyId
    );


}