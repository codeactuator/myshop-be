package com.skcodify.myshop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // The H2 console is handled by webSecurityCustomizer, so we configure the rest here.
            // For development, we can disable CSRF for all other requests for simplicity.
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // All other requests are permitted for now (for development convenience)
                // You can change this to .anyRequest().authenticated() or more specific rules later
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Completely ignore the H2 console path from Spring Security filter chain
        // This is the correct way to allow H2 console access with Spring Security
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }
}