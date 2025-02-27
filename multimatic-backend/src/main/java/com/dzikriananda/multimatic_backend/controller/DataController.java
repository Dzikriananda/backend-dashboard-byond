package com.dzikriananda.multimatic_backend.controller;

import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.interfaces.DataService;
import com.dzikriananda.multimatic_backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
//    @GetMapping("/all-review")
//    public ResponseEntity<List<ByondReview>> allReview() {
//        List<ByondReview> data = dataService.findAllReview();
//        return ResponseEntity.ok(data);
//    }

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
        System.out.println("offset" + offset);
        System.out.println("keyword" + keyword);
        List<ByondReview> data = dataService.findPriorityReviewBySearch(offset,keyword);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/app-detail")
    public ResponseEntity<AppDetail> appDetail() {
        String url = "http://flask-app:5000/fetch_app_details";
        return restTemplate.getForEntity(url, AppDetail.class);
    }
}
