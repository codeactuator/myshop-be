package com.skcodify.myshop.controller;

import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.ProductDto;
import com.skcodify.myshop.repository.UserRepository;
import com.skcodify.myshop.service.CloudStorageService;
import com.skcodify.myshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CloudStorageService cloudStorageService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService, CloudStorageService cloudStorageService, UserRepository userRepository) {
        this.productService = productService;
        this.cloudStorageService = cloudStorageService;
        this.userRepository = userRepository;
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
                                                       @RequestParam(required = false) Long userId,
                                                       Principal principal) {
        Long societyId = null;
        if (principal != null) {
            String username = principal.getName();
            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByPhone(username);
            }
            if (userOpt.isPresent() && userOpt.get().getSociety() != null) {
                societyId = userOpt.get().getSociety().getId();
            }
        }
        return ResponseEntity.ok(productService.findProducts(status, userId, societyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
        ProductDto updated = productService.updateProduct(id, productDto);
        if (updated.getUserId() != null) {
            NotificationController.sendNotification(
                String.valueOf(updated.getUserId()),
                "alert",
                "Your product '" + updated.getName() + "' was successfully updated!"
            );
        }
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto created = productService.createProduct(productDto);
        if (created.getUserId() != null) {
            NotificationController.sendNotification(
                String.valueOf(created.getUserId()),
                "alert",
                "Your new product '" + created.getName() + "' is now live!"
            );
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}