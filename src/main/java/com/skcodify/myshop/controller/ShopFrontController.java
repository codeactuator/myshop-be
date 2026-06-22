package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.ShopFrontDto;
import com.skcodify.myshop.service.ShopFrontService;
import com.skcodify.myshop.service.CloudStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shop-front")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class ShopFrontController {

    private final ShopFrontService shopFrontService;
    private final CloudStorageService cloudStorageService;

    public ShopFrontController(ShopFrontService shopFrontService, CloudStorageService cloudStorageService) {
        this.shopFrontService = shopFrontService;
        this.cloudStorageService = cloudStorageService;
    }

    @PostMapping(value = "/upload-banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBanner(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudStorageService.uploadShopBanner(file);
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IllegalArgumentException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Invalid request parameter";
            return ResponseEntity.badRequest().body(Map.of("error", message));
        } catch (IOException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Unknown I/O error";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload banner: " + message));
        }
    }

    @GetMapping
    public ResponseEntity<ShopFrontDto> getShopFront(@RequestParam Long sellerId) {
        return ResponseEntity.ok(shopFrontService.getShopFrontBySellerId(sellerId));
    }

    @PutMapping
    public ResponseEntity<ShopFrontDto> updateShopFront(@RequestParam Long sellerId, @RequestBody ShopFrontDto shopFrontDto) {
        return ResponseEntity.ok(shopFrontService.updateShopFront(sellerId, shopFrontDto));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<ShopFrontDto>> getShopFrontsInBatch(@RequestParam List<Long> sellerIds) {
        return ResponseEntity.ok(shopFrontService.getShopFrontsBySellerIds(sellerIds));
    }
}