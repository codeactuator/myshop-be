package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.mapper.ProductMapper;
import com.skcodify.myshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> findProducts(String status) {
        List<Product> products;
        if (status != null && !status.isEmpty()) {
            products = productRepository.findByStatus(status);
        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto findProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }
}