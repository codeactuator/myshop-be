package com.skcodify.myshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.ShopFront;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.mapper.ProductMapper;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.ShopFrontRepository;
import com.skcodify.myshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShopFrontRepository shopFrontRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, ShopFrontRepository shopFrontRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.shopFrontRepository = shopFrontRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> findProducts(String status, Long userId, Long societyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedDate").descending());
        List<Product> products;
        
        if (societyId != null && societyId != 0) {
            if (status != null && !status.isEmpty()) {
                Page<Product> productPage = productRepository.findByStatusAndSocietyId(status, societyId, pageable);
                products = productPage.getContent();
            } else {
                products = productRepository.findBySocietyId(societyId);
            }
        }
        else if (userId != null) {
            products = productRepository.findBySellerId(userId);
        } else if (status != null && !status.isEmpty()) {
            Page<Product> productPage = productRepository.findByStatus(status, pageable);
            products = productPage.getContent();
        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .map(product -> {
                    ProductDto dto = productMapper.toDto(product);
                    User seller = product.getSeller();
                    if (seller != null) {
                        dto.setSellerName(seller.getName());
                        dto.setVerifiedSeller(seller.isVerified());
                        
                        // Resolve ShopFront options server-side
                        ShopFront sf = seller.getShopFront();
                        if (sf != null) {
                            dto.setShopName(sf.getShopName());
                            dto.setShopTagline(sf.getShopTagline());
                            dto.setSellerProfileImageUrl(sf.getProfileImageUrl());
                        } else {
                            dto.setShopName(seller.getShopName() != null ? seller.getShopName() : seller.getName());
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDto findProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @CachePut(value = "products", key = "#id")
    @Transactional
    public ProductDto updateProduct(String id, ProductDto updates) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (updates.getName() != null) {
            product.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            product.setDescription(updates.getDescription());
        }
        if (updates.getPrice() != null) {
            product.setPrice(updates.getPrice());
        }
        if (updates.getCategory() != null) {
            product.setCategory(updates.getCategory());
        }
        if (updates.getStock() != null) {
            product.setStock(updates.getStock());
        }
        if (updates.getStatus() != null) {
            product.setStatus(updates.getStatus());
        }
        if (updates.getUserId() != null && !updates.getUserId().isEmpty()) {
            User seller = userRepository.findById(Long.valueOf(updates.getUserId()))
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found with id: " + updates.getUserId()));
            product.setSeller(seller);
        }
        if (updates.getImageUrls() != null) {
            // Clear existing images and add new ones to ensure Hibernate detects collection changes
            // This is a more robust way to update @ElementCollection
            if (product.getImageUrls() == null) {
                product.setImageUrls(new ArrayList<>());
            } else {
                product.getImageUrls().clear();
            }
            if (!updates.getImageUrls().isEmpty()) {
                product.getImageUrls().addAll(updates.getImageUrls());
            }
        } else {
            // Explicitly handle null to avoid lazy-loading issues if the collection was null
            product.setImageUrls(new ArrayList<>());
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        
        // Handle manual ID assignment to prevent 400 Bad Request on GET /products/{id}
        if (productDto.getId() != null && !productDto.getId().isEmpty()) {
            product.setId(productDto.getId());
        } else {
            product.setId("prod-" + UUID.randomUUID().toString().substring(0, 8));
        }

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setStatus(productDto.getStatus() != null ? productDto.getStatus() : "available");
        
        if (productDto.getUserId() != null && !productDto.getUserId().isEmpty()) {
            User seller = userRepository.findById(Long.valueOf(productDto.getUserId()))
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found with id: " + productDto.getUserId()));
            product.setSeller(seller);
        }

        product.setStock(productDto.getStock());

        if (productDto.getImageUrls() != null) {
            product.setImageUrls(new ArrayList<>(productDto.getImageUrls()));
        }

        return productMapper.toDto(productRepository.save(product));
    }
}