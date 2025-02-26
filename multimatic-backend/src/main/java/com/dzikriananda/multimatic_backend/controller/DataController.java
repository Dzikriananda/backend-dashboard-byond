package com.dzikriananda.multimatic_backend.controller;

import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.interfaces.DataService;
import com.dzikriananda.multimatic_backend.model.ByondReview;
import com.dzikriananda.multimatic_backend.model.DaySentiment;
import com.dzikriananda.multimatic_backend.model.SentimentDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/data")
public class DataController {

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
}
