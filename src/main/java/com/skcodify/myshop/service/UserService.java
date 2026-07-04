package com.skcodify.myshop.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skcodify.myshop.domain.Society;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.domain.UserType;
import com.skcodify.myshop.dto.UserDto;
import com.skcodify.myshop.mapper.UserMapper;
import com.skcodify.myshop.repository.SocietyRepository;
import com.skcodify.myshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SocietyRepository societyRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, SocietyRepository societyRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.societyRepository = societyRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findUsers(List<Long> ids, String phone) {
        List<User> users;
        if (phone != null && !phone.isEmpty()) {
            // Prioritize search by phone since it's unique
            users = userRepository.findByPhone(phone).map(List::of).orElse(List.of());
        } else if (ids != null && !ids.isEmpty()) {
            users = userRepository.findAllById(ids);
        } else {
            // Default to finding all users if no specific criteria is given
            users = userRepository.findAll();
        }
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.getServiceSocieties().size(); // Force initialization of the lazy collection inside transaction
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> findUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .map(user -> {
                    user.getServiceSocieties().size(); // Force initialization of lazy collection
                    return userMapper.toDto(user);
                });
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByPhone(userDto.getPhone()).isPresent()) {
            throw new RuntimeException("A user with this phone number already exists.");
        }
        User user = userMapper.toEntity(userDto);

        return saveUser(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (updates.getName() != null) user.setName(updates.getName());
        if (updates.getEmail() != null) user.setEmail(updates.getEmail());
        if (updates.getApartmentNumber() != null) user.setApartmentNumber(updates.getApartmentNumber());
        if (updates.getUserType() != null) user.setUserType(updates.getUserType());
        if (updates.getShopName() != null) user.setShopName(updates.getShopName());

        // Handle unified serviceSocieties mapping with role-based backend validation
        if (updates.getServiceSocieties() != null) {
            // Backend Validation: Buyers can belong to at most one society
            if (user.getUserType() != UserType.SELLER && updates.getServiceSocieties().size() > 1) {
                throw new IllegalArgumentException("Buyers can select at most one society.");
            }

            List<Long> targetIds = updates.getServiceSocieties().stream()
                    .map(s -> s.getId())
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            List<Society> societies = societyRepository.findAllById(targetIds);
            if (societies.size() != targetIds.size()) {
                throw new EntityNotFoundException("One or more selected societies could not be found.");
            }
            
            user.getServiceSocieties().clear();
            user.getServiceSocieties().addAll(societies);
        }

        if (updates.isVerified()) user.setVerified(updates.isVerified());
        if (updates.isBlocked()) user.setBlocked(updates.isBlocked());

        return saveUser(user);

    }

    private UserDto saveUser(User user) { 
        User savedUser = userRepository.saveAndFlush(user);
        // Force loading of the collection to ensure it is populated within the transaction
        savedUser.getServiceSocieties().size();
        
        UserDto userDto = userMapper.toDto(savedUser);
        
        // Manually map societies to DTO as a bulletproof fallback if MapStruct skipped it
        if (savedUser.getServiceSocieties() != null) {
            userDto.setServiceSocieties(savedUser.getServiceSocieties().stream()
                .map(soc -> {
                    com.skcodify.myshop.dto.SocietyDto dto = new com.skcodify.myshop.dto.SocietyDto();
                    dto.setId(soc.getId());
                    dto.setName(soc.getName());
                    dto.setArea(soc.getArea());
                    return dto;
                })
                .collect(Collectors.toSet()));
        }
        
        return userDto;
    }
}