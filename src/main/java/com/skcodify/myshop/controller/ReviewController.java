package com.skcodify.myshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.skcodify.myshop.dto.ReviewDto;
import com.skcodify.myshop.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewDto> getReviews(@RequestParam String productId) {
        return reviewService.findReviewsByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@RequestBody ReviewDto reviewDto) {
        return reviewService.createReview(reviewDto);
    }


}