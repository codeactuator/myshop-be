package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.User;
import org.springframework.stereotype.Component;

import com.skcodify.myshop.domain.Review;
import com.skcodify.myshop.dto.ReviewDto;

import java.util.UUID;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setDate(review.getReviewDate());
        if (review.getProduct() != null) {
            dto.setProductId(review.getProduct().getId());
        }
        if (review.getUser() != null) {
            dto.setUserId(review.getUser().getId());
        }
        return dto;
    }

    public Review toEntity(ReviewDto dto, Product product, User user) {
        if (dto == null) {
            return null;
        }

        Review review = new Review();
        review.setId(UUID.randomUUID().toString()); // Generate a new ID for the review
        review.setProduct(product);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewDate(dto.getDate());
        return review;
    }
}