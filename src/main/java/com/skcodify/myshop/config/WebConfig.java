package com.skcodify.myshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global configuration for the web layer, including CORS settings.
 */
@Configuration
public class WebConfig {

    /**
     * Configures CORS to allow cross-origin requests to the API.
     * @return A WebMvcConfigurer with the defined CORS mappings.
     */
    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**") // Apply this policy to all endpoints
    //                     .allowedOrigins("http://localhost:3000") // Allow requests from any origin. For production, you should restrict this to your frontend's domain, e.g., "http://localhost:3000".
    //                     .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Specify the allowed HTTP methods
    //                     .allowedHeaders("*"); // Allow all headers in the request
    //         }
    //     };
    // }
}