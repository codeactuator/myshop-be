package com.skcodify.myshop.mapper;

import org.springframework.stereotype.Component;

import com.skcodify.myshop.domain.Review;
import com.skcodify.myshop.dto.ReviewDto;

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
}