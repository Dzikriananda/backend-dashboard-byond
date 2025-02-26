package com.dzikriananda.multimatic_backend.config;


import com.dzikriananda.multimatic_backend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter createJwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
