package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByStatus(String status);

    List<Product> findByUserId(Long userId);

}