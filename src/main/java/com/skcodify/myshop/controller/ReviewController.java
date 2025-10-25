package com.skcodify.myshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skcodify.myshop.dto.ReviewDto;
import com.skcodify.myshop.service.ReviewService;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*") // Allow requests from any origin
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewDto> getReviews(@RequestParam String productId) {
        return reviewService.findReviewsByProductId(productId);
    }
}