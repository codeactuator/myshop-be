package com.skcodify.myshop.controller;

import com.skcodify.myshop.dto.UserDto;
import com.skcodify.myshop.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(required = false) String phone) {
        return userService.findUsers(ids, phone);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/by-phone/{phone}")
    public UserDto getUserByPhone(@PathVariable String phone) {
        return userService.findUserByPhone(phone);
    }
}