package com.skcodify.myshop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skcodify.myshop.domain.ShopFront;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.domain.UserType;
import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.dto.ShopFrontDto;
import com.skcodify.myshop.mapper.ProductMapper;
import com.skcodify.myshop.mapper.ShopFrontMapper;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.ShopFrontRepository;
import com.skcodify.myshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ShopFrontService {

    private final ShopFrontRepository shopFrontRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShopFrontMapper shopFrontMapper;
    private final ProductMapper productMapper;

    public ShopFrontService(ShopFrontRepository shopFrontRepository, UserRepository userRepository, ProductRepository productRepository, ShopFrontMapper shopFrontMapper, ProductMapper productMapper) {
        this.shopFrontRepository = shopFrontRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.shopFrontMapper = shopFrontMapper;
        this.productMapper = productMapper;
    }

    @Cacheable(value = "shopFronts", key = "#sellerId")
    @Transactional
    public ShopFrontDto getShopFrontBySellerId(Long sellerId) {
        ShopFront shopFront = findOrCreateShopFrontBySellerId(sellerId);
        ShopFrontDto dto = shopFrontMapper.toDto(shopFront);
        User user = shopFront.getUser();
        if (user != null) {
            enrichDtoWithUserMetadata(dto, user);
        }
        enrichDtoWithPopularProducts(dto, sellerId);
        return dto;
    }

    @CacheEvict(value = "shopFronts", allEntries = true)
    @Transactional
    public ShopFrontDto updateShopFront(Long sellerId, ShopFrontDto shopFrontDto) {
        ShopFront shopFront = findOrCreateShopFrontBySellerId(sellerId);
        shopFrontMapper.updateEntityFromDto(shopFront, shopFrontDto);
        ShopFront updatedShopFront = shopFrontRepository.save(shopFront);
        ShopFrontDto dto = shopFrontMapper.toDto(updatedShopFront);
        if (updatedShopFront.getUser() != null) {
            enrichDtoWithUserMetadata(dto, updatedShopFront.getUser());
        }
        enrichDtoWithPopularProducts(dto, sellerId);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ShopFrontDto> searchShops(Long societyId, String query) {
        // Pull all sellers who service this specific society
        List<User> sellers = userRepository.findAll().stream()
                .filter(u -> u.getUserType() == UserType.SELLER && !u.isBlocked())
                .filter(u -> societyId == null || societyId == 0 || 
                        (u.getServiceSocieties() != null && u.getServiceSocieties().stream().anyMatch(s -> s.getId().equals(societyId))))
                .collect(Collectors.toList());

        List<Long> sellerIds = sellers.stream().map(User::getId).collect(Collectors.toList());
        List<ShopFront> shopFronts = shopFrontRepository.findByUserIdIn(sellerIds);

        return shopFronts.stream()
                .map(sf -> {
                    ShopFrontDto dto = shopFrontMapper.toDto(sf);
                    enrichDtoWithUserMetadata(dto, sf.getUser());
                    enrichDtoWithPopularProducts(dto, sf.getUser().getId());
                    return dto;
                })
                .filter(dto -> query == null || query.isEmpty() || 
                        (dto.getShopName() != null && dto.getShopName().toLowerCase().contains(query.toLowerCase())) ||
                        (dto.getPayeeName() != null && dto.getPayeeName().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
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
        return existingShopFronts.stream()
                .filter(sf -> sf.getUser() != null && !sf.getUser().isBlocked()) // Do not list shops that are disabled/blocked by admin
                .map(sf -> {
                    ShopFrontDto dto = shopFrontMapper.toDto(sf);
                    enrichDtoWithUserMetadata(dto, sf.getUser());
                    enrichDtoWithPopularProducts(dto, sf.getUser().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void enrichDtoWithUserMetadata(ShopFrontDto dto, User user) {
        dto.setVerified(user.isVerified());
        dto.setBlocked(user.isBlocked());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
    }

    private void enrichDtoWithPopularProducts(ShopFrontDto dto, Long sellerId) {
        // Pull the top 3 available products for this seller directly from the database
        List<com.skcodify.myshop.domain.Product> products = productRepository.findBySellerId(sellerId);
        List<ProductDto> popular = products.stream()
                .filter(p -> "available".equalsIgnoreCase(p.getStatus()))
                .filter(p -> p.getSeller() != null && !p.getSeller().isBlocked()) // Ensure product is not listed if the seller is disabled/blocked by admin
                .limit(3)
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        dto.setPopularProducts(popular);
    }

    private ShopFront findOrCreateShopFrontBySellerId(Long sellerId) {
        List<ShopFront> shopFronts = shopFrontRepository.findAll().stream()
                .filter(sf -> sf.getUser() != null && sellerId.equals(sf.getUser().getId()))
                .collect(Collectors.toList());

        if (!shopFronts.isEmpty()) {
            ShopFront primary = shopFronts.get(0);
            if (shopFronts.size() > 1) {
                List<ShopFront> duplicates = shopFronts.subList(1, shopFronts.size());
                shopFrontRepository.deleteAll(duplicates);
            }
            return primary;
        }

        // If no shop front exists for this seller, create a new one
        User user = userRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + sellerId));
        if (user.getUserType() != UserType.SELLER) {
            throw new IllegalArgumentException("User with id " + sellerId + " is not a seller.");
        }
        ShopFront newShopFront = new ShopFront();
        newShopFront.setUser(user);
        newShopFront.setThemeColor("#FFFFFF"); // Set default values if needed
        return shopFrontRepository.save(newShopFront);
    }
}