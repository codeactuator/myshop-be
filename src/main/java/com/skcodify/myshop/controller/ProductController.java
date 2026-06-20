package com.skcodify.myshop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.service.CloudStorageService;
import com.skcodify.myshop.service.ProductService;

@RestController
@RequestMapping("/products")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class ProductController {

    private final ProductService productService;
    private final CloudStorageService cloudStorageService;

    public ProductController(ProductService productService, CloudStorageService cloudStorageService) {
        this.productService = productService;
        this.cloudStorageService = cloudStorageService;
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudStorageService.uploadProductImage(file);
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IllegalArgumentException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Invalid request parameter";
            return ResponseEntity.badRequest().body(Map.of("error", message));
        } catch (IOException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Unknown I/O error";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image: " + message));
        }
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

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }
}