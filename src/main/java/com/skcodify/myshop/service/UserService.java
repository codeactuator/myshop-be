package com.skcodify.myshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.UserDto;
import com.skcodify.myshop.mapper.UserMapper;
import com.skcodify.myshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
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
        return userMapper.toDto(user);
    }

    public Optional<UserDto> findUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .map(userMapper::toDto);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByPhone(userDto.getPhone()).isPresent()) {
            throw new RuntimeException("A user with this phone number already exists.");
        }
        User user = userMapper.toEntity(userDto);
        return saveUser(user);
    }

    private UserDto saveUser(User user) { 
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}