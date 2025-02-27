package com.dzikriananda.multimatic_backend.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> generalBuckets = new ConcurrentHashMap<>();

    @Autowired
    public RateLimitingFilter() {
        // No-arg constructor
    }

    private Bucket createBucket(int limit) {
        return Bucket.builder()
                .addLimit(io.github.bucket4j.Bandwidth.simple(limit, java.time.Duration.ofMinutes(1)))
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        String key = httpRequest.getRemoteAddr(); // Rate limit per IP (or use email for login)

        Bucket bucket;

        if (path.startsWith("/api/auth/login")) {
            // 5 requests per minute for login
            bucket = loginBuckets.computeIfAbsent(key, k -> createBucket(5));
        } else {
            // 30 requests per minute for other APIs
            System.out.println("no login");
            bucket = generalBuckets.computeIfAbsent(key, k -> createBucket(30));
        }

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("{\"message\": \"Too many requests. Try again later.\"}");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}
