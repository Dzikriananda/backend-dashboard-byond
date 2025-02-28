package com.dzikriananda.multimatic_backend.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.time.Duration;

@Service
public class FlaskService {
    private final String url = "http://flask-app:5000/fetch_reviews";
    private final RestTemplate restTemplate;

    public FlaskService() {
        this.restTemplate = createRestTemplate();
    }

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10 * 1000); // 10 seconds
        factory.setReadTimeout(10 * 60 * 1000); // 10 minutes (600,000 ms)
        return new RestTemplate(factory);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at 00:00
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void callFlaskService() {
        try {
            System.out.println("Calling Flask API...");

            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Flask API Response: " + response);
        } catch (Exception e) {
            System.err.println("Error calling Flask API: " + e.getMessage());
            throw e; // Ensures retry happens
        }
    }
}
