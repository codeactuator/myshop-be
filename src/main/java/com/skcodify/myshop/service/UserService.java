package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUsers(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllById(ids);
        }
        return userRepository.findAll();
    }
}