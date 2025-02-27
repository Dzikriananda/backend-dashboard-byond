package com.dzikriananda.multimatic_backend.config;

import com.dzikriananda.multimatic_backend.filter.JwtAuthenticationFilter;
import com.dzikriananda.multimatic_backend.filter.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**",
            "/api/auth/**",
            "/api/auth/login",
            "/api/auth/register"

    };

    @Autowired
    CorsConfig corsConfiguration;


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable().
                headers(headers ->
                        // Configure XSS protection header ANTI XSS INJECTION BOSSS
                        headers.
                                xssProtection(
                                        xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                                ).contentSecurityPolicy(
                                        // Configure Content Security Policy to allow scripts only from 'self'
                                        cps -> cps.policyDirectives("script-src 'self'")
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll() // Allow all requests to /auth/**
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .cors(c -> c.configurationSource(corsConfiguration))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add the filter
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
