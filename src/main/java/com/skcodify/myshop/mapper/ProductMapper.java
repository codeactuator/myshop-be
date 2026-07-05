package com.skcodify.myshop.mapper;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.SocietyDto;
import com.skcodify.myshop.dto.ProductDto;


@Component
public class ProductMapper {

    @Autowired
    public ProductMapper() {
    }


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

        if (product.getSeller() != null) {
            User seller = product.getSeller();
            if (seller.getServiceSocieties() != null) {
                dto.setServiceSocieties(seller.getServiceSocieties().stream()
                    .map(society -> {
                        SocietyDto sDto = new SocietyDto();
                        sDto.setId(society.getId());
                        sDto.setName(society.getName());
                        sDto.setArea(society.getArea());
                        return sDto;
                    })
                    .collect(Collectors.toSet())
                );
            }
        }

        if (product.getSeller() != null) {
            dto.setUserId(String.valueOf(product.getSeller().getId()));
        }

        return dto;
    }
}