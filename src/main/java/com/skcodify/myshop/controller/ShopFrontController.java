package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.ShopFrontDto;
import com.skcodify.myshop.service.ShopFrontService;
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
    public ShopFrontDto getShopFront(@RequestParam Long sellerId) {
        return shopFrontService.getShopFrontBySellerId(sellerId);
    }

    @PutMapping
    public ShopFrontDto updateShopFront(@RequestParam Long sellerId, @RequestBody ShopFrontDto shopFrontDto) {
        return shopFrontService.updateShopFront(sellerId, shopFrontDto);
    }

    @GetMapping("/batch")
    public List<ShopFrontDto> getShopFrontsInBatch(@RequestParam List<Long> sellerIds) {
        return shopFrontService.getShopFrontsBySellerIds(sellerIds);
    }
}