package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.ShopFrontDto;
import com.skcodify.myshop.service.ShopFrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop-front")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class ShopFrontController {

    private final ShopFrontService shopFrontService;

    public ShopFrontController(ShopFrontService shopFrontService) {
        this.shopFrontService = shopFrontService;
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