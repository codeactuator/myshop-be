package com.skcodify.myshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*") // Allow requests from any origin
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam(required = false) String status) {
        return productService.findProducts(status);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable String id) {
        return productService.findProductById(id);
    }
}