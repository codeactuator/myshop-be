package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setApartmentNumber(user.getApartmentNumber());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserType(user.getUserType());
        dto.setShopName(user.getShopName());
        dto.setVerified(user.isVerified());
        dto.setBlocked(user.isBlocked());

        return dto;
    }
}