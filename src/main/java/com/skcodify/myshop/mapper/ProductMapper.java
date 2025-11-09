package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setImageUrls(product.getImageUrls());
        dto.setStatus(product.getStatus());
        dto.setPostedDate(product.getPostedDate());
        dto.setStock(product.getStock());

        if (product.getUserId() != null) {
            dto.setUserId(String.valueOf(product.getUserId()));
        }

        return dto;
    }
}