package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.Review;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.ReviewDto;
import com.skcodify.myshop.mapper.ReviewMapper;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.ReviewRepository;
import com.skcodify.myshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Cacheable(value = "reviews", key = "#productId")
    @Transactional(readOnly = true)
    public List<ReviewDto> findReviewsByProductId(String productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "reviews", key = "#reviewDto.productId")
    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto) {
        Product product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + reviewDto.getProductId()));

        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + reviewDto.getUserId()));

        Review review = reviewMapper.toEntity(reviewDto, product, user);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }
}
