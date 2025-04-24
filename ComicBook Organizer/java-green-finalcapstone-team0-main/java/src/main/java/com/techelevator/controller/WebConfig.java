package com.techelevator.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS requests from the frontend (localhost:5175) to backend (localhost:9000)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Your frontend origin
                .allowedOrigins("http://localhost:5174") // Your frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // Allow credentials like cookies or tokens

    }
}
