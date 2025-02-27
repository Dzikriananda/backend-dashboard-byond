package com.dzikriananda.multimatic_backend.controller;

import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.interfaces.DataService;
import com.dzikriananda.multimatic_backend.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DataService dataService;

    @GetMapping("/all-sentiment")
    public ResponseEntity<List<DaySentiment>> allSentiment(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<DaySentiment> data = dataService.findAllSentiment(startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/all-review")
    public ResponseEntity<List<ByondReview>> allReview() {
        List<ByondReview> data = dataService.findAllReview();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/sentiment-distribution")
    public ResponseEntity<List<SentimentDistribution>> sentimentDistribution(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<SentimentDistribution> data = dataService.findSentimentDistribution(startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/score-frequency")
    public ResponseEntity<List<ScoreFrequency>> scoreFrequency(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<ScoreFrequency> data = dataService.findScoreFrequency(startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/sentiment-cloud")
    public ResponseEntity<List<SentimentCloud>> sentimentCloud(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<SentimentCloud> data = dataService.findSentimentCloud(startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/priority-review")
    public ResponseEntity<List<ByondReview>> priorityReview(
            @RequestParam int offset,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<ByondReview> data = dataService.findPriorityReview(offset, startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/priority-review/search")
    public ResponseEntity<List<ByondReview>> priorityReviewBySearch(
            @RequestParam int offset,
            @RequestParam String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<ByondReview> data = dataService.findPriorityReviewBySearch(offset, keyword, startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/app-detail")
    public ResponseEntity<AppDetail> appDetail() throws JsonProcessingException {
        String url = "http://flask-app:5000/fetch_app_details";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Connection", "keep-alive");
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        ResponseEntity<AppDetail> response = restTemplate.exchange(url, HttpMethod.GET, entity, AppDetail.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(response.getBody());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(responseBody.getBytes(StandardCharsets.UTF_8).length);
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.set("Connection", "keep-alive");

        return new ResponseEntity<>(response.getBody(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/latest-review-date")
    public ResponseEntity<LatestDate> latestDate() {
        LatestDate date = dataService.findLatestReviewDate();
        return ResponseEntity.ok().body(date);
    }
}
