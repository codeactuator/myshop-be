package com.skcodify.myshop.repository;

import com.skcodify.myshop.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    List<Review> findByProductId(String productId);
}