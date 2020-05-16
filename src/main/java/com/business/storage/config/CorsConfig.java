package com.business.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: mmustafin@context-it.ru
 * @created: 15.05.2020
 */
@Configuration
public class CorsConfig {

    private static final String FRONTEND_URL = "http://localhost:3000";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/question/**").allowedOrigins(FRONTEND_URL);
            }
        };
    }

}
