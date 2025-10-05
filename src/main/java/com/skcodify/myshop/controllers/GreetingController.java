package com.skcodify.myshop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    /**
     * Responds to GET requests at /api/greetings
     * @return A generic greeting.
     */
    @GetMapping
    public String sayHello() {
        return "Hello, World!";
    }

}