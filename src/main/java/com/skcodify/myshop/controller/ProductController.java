package com.skcodify.myshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false) String status,
                                                       @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(productService.findProducts(status, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }
}