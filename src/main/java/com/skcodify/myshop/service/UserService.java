package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.UserDto;
import com.skcodify.myshop.mapper.UserMapper;
import com.skcodify.myshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public UserDto findUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("User not found with phone: " + phone));
        return userMapper.toDto(user);
    }
}