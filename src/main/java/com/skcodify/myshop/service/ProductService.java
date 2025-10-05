package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findProducts(String status) {
        if (status != null && !status.isEmpty()) {
            return productRepository.findByStatus(status);
        }
        return productRepository.findAll();
    }
}