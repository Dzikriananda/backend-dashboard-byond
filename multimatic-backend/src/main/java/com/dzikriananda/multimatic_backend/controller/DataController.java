package com.dzikriananda.multimatic_backend.controller;

import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.interfaces.DataService;
import com.dzikriananda.multimatic_backend.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DataService dataService;
    @GetMapping("/all-sentiment")
    public ResponseEntity<List<DaySentiment>> allSentiment() {
        List<DaySentiment> data = dataService.findAllSentiment();
        return ResponseEntity.ok(data);
    }
    @GetMapping("/all-review")
    public ResponseEntity<List<ByondReview>> allReview() {
        List<ByondReview> data = dataService.findAllReview();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/sentiment-distribution")
    public ResponseEntity<List<SentimentDistribution>> sentimentDistribution() {
        List<SentimentDistribution> data = dataService.findSentimentDistribution();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/score-frequency")
    public ResponseEntity<List<ScoreFrequency>> scoreFrequency() {
        List<ScoreFrequency> data = dataService.findScoreFrequency();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/sentiment-cloud")
    public ResponseEntity<List<SentimentCloud>> sentimentCloud() {
        List<SentimentCloud> data = dataService.findSentimentCloud();

        return ResponseEntity.ok(data);
    }

    @GetMapping("/priority-review")
    public ResponseEntity<List<ByondReview>> priorityReview(@RequestParam int offset) {
        List<ByondReview> data = dataService.findPriorityReview(offset);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/priority-review/search")
    public ResponseEntity<List<ByondReview>> priorityReviewBySearch(@RequestParam int offset,@RequestParam String keyword) {
        List<ByondReview> data = dataService.findPriorityReviewBySearch(offset,keyword);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/app-detail")
    public ResponseEntity<AppDetail> appDetail() throws JsonProcessingException {
        String url = "http://flask-app:5000/fetch_app_details";

        // Set request headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Connection", "keep-alive");
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        ResponseEntity<AppDetail> response = restTemplate.exchange(url, HttpMethod.GET, entity, AppDetail.class);

        // Ensure response body is not null
        response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(response.getBody()); // Convert to JSON string

        // Set response headers
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(responseBody.getBytes(StandardCharsets.UTF_8).length);
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.set("Connection", "keep-alive"); // 🔥 Force keep-alive in response

        return new ResponseEntity<>(response.getBody(), responseHeaders, HttpStatus.OK);

    }


    @GetMapping("/latest-review-date")
    public ResponseEntity<LatestDate> latestDate() {
        LatestDate date = dataService.findLatestReviewDate();
        return ResponseEntity.ok().body(date);
    }
}
