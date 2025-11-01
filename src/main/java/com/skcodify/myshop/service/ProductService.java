package com.skcodify.myshop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.mapper.ProductMapper;
import com.skcodify.myshop.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;

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
        if (updates.getStock() != null) { // This was causing the compilation error
            product.setStock(updates.getStock());
        }
        if (updates.getStatus() != null) {
            product.setStatus(updates.getStatus());
        }
        if (updates.getImageUrls() != null) {
            // Clear existing images and add new ones to ensure Hibernate detects collection changes
            // This is a more robust way to update @ElementCollection
            product.getImageUrls().clear();
            if (!updates.getImageUrls().isEmpty()) {
                product.getImageUrls().addAll(updates.getImageUrls());
            }
        }
        return productMapper.toDto(productRepository.save(product));
    }
}