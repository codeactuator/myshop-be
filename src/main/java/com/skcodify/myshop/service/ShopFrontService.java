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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public ShopFrontDto getShopFrontBySellerId(Long sellerId) {
        ShopFront shopFront = findOrCreateShopFrontBySellerId(sellerId);
        return shopFrontMapper.toDto(shopFront);
    }

    @Transactional
    public ShopFrontDto updateShopFront(Long sellerId, ShopFrontDto shopFrontDto) {
        ShopFront shopFront = findOrCreateShopFrontBySellerId(sellerId);
        shopFrontMapper.updateEntityFromDto(shopFront, shopFrontDto);
        ShopFront updatedShopFront = shopFrontRepository.save(shopFront);
        return shopFrontMapper.toDto(updatedShopFront);
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