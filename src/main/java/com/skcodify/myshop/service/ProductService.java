package com.skcodify.myshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.mapper.ProductMapper;
import com.skcodify.myshop.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> findProducts(String status, Long userId) {
        List<Product> products;
        if (userId != null) {
            products = productRepository.findByUserId(userId);
        } else if (status != null && !status.isEmpty()) {
            products = productRepository.findByStatus(status);
        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .map(productMapper::toDto)
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
            product.setUserId(Long.valueOf(updates.getUserId()));
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
            product.setUserId(Long.valueOf(productDto.getUserId()));
        }

        product.setStock(productDto.getStock());

        if (productDto.getImageUrls() != null) {
            product.setImageUrls(new ArrayList<>(productDto.getImageUrls()));
        }

        return productMapper.toDto(productRepository.save(product));
    }
}