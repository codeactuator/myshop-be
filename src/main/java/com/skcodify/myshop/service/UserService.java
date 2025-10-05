package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.dto.UserDto;
import com.skcodify.myshop.mapper.UserMapper;
import com.skcodify.myshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findUsers(List<Long> ids) {
        List<User> users;
        if (ids != null && !ids.isEmpty()) {
            users = userRepository.findAllById(ids);
        } else {
            users = userRepository.findAll();
        }
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}