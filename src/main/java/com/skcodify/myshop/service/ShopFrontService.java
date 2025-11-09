package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.ShopFront;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.domain.UserType;
import com.skcodify.myshop.dto.ShopFrontDto;
import com.skcodify.myshop.mapper.ShopFrontMapper;
import com.skcodify.myshop.repository.ShopFrontRepository;
import com.skcodify.myshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopFrontService {

    private final ShopFrontRepository shopFrontRepository;
    private final UserRepository userRepository;
    private final ShopFrontMapper shopFrontMapper;

    public ShopFrontService(ShopFrontRepository shopFrontRepository, UserRepository userRepository, ShopFrontMapper shopFrontMapper) {
        this.shopFrontRepository = shopFrontRepository;
        this.userRepository = userRepository;
        this.shopFrontMapper = shopFrontMapper;
    }

    @Cacheable(value = "shopFronts", key = "#sellerId")
    @Transactional(readOnly = true)
    public ShopFrontDto getShopFrontBySellerId(Long sellerId) {
        ShopFront shopFront = findOrCreateShopFrontBySellerId(sellerId);
        return shopFrontMapper.toDto(shopFront);
    }

    @CachePut(value = "shopFronts", key = "#sellerId")
    @Transactional
    public ShopFrontDto updateShopFront(Long sellerId, ShopFrontDto shopFrontDto) {
        ShopFront shopFront = findOrCreateShopFrontBySellerId(sellerId);
        shopFrontMapper.updateEntityFromDto(shopFront, shopFrontDto);
        ShopFront updatedShopFront = shopFrontRepository.save(shopFront);
        return shopFrontMapper.toDto(updatedShopFront);
    }

    @Transactional
    @Cacheable(value = "shopFronts", key = "'batch-' + #sellerIds.toString()")
    public List<ShopFrontDto> getShopFrontsBySellerIds(List<Long> sellerIds) {
        List<ShopFront> existingShopFronts = shopFrontRepository.findByUserIdIn(sellerIds);
        List<Long> foundSellerIds = existingShopFronts.stream()
                .map(sf -> sf.getUser().getId())
                .collect(Collectors.toList());

        List<Long> missingSellerIds = sellerIds.stream()
                .filter(id -> !foundSellerIds.contains(id))
                .collect(Collectors.toList());

        List<ShopFront> createdShopFronts = missingSellerIds.stream()
                .map(this::findOrCreateShopFrontBySellerId)
                .collect(Collectors.toList());

        existingShopFronts.addAll(createdShopFronts);
        return existingShopFronts.stream().map(shopFrontMapper::toDto).collect(Collectors.toList());
    }

    private ShopFront findOrCreateShopFrontBySellerId(Long sellerId) {
        return shopFrontRepository.findByUserId(sellerId)
                .orElseGet(() -> {
                    User user = userRepository.findById(sellerId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + sellerId));

                    if (user.getUserType() != UserType.SELLER) {
                        throw new IllegalArgumentException("User with id " + sellerId + " is not a seller.");
                    }

                    ShopFront newShopFront = new ShopFront();
                    newShopFront.setUser(user);
                    // Set default values if needed
                    newShopFront.setThemeColor("#FFFFFF");
                    return shopFrontRepository.save(newShopFront);
                });
    }
}