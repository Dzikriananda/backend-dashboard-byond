package com.dzikriananda.multimatic_backend.service;

import com.dzikriananda.multimatic_backend.interfaces.DataService;
import com.dzikriananda.multimatic_backend.model.*;
import com.dzikriananda.multimatic_backend.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DataServiceImpl implements DataService {

    @Autowired
    DataRepository dataRepository;


    @Override
    public List<ByondReview> findAllReview() {
        return dataRepository.findAllData();
    }

    @Override
    public List<DaySentiment> findAllSentiment() {
        List<Object[]> rawResults = dataRepository.findAllSentiment();

        return rawResults.stream()
                .map(row -> new DaySentiment(
                        ((java.sql.Date) row[0]).toLocalDate(),  // Convert SQL Date to LocalDate
                        String.valueOf(row[1]),  // Sentiment
                        ((Number) row[2]).intValue()  // Convert COUNT(*) to Integer
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<SentimentDistribution> findSentimentDistribution() {
        List<Object[]> rawResults = dataRepository.findDistributionSentiment();
        return rawResults.stream()
                .map(row -> new SentimentDistribution(
                        row[0] != null ? (String) row[0] : "Unknown",
                        String.valueOf(row[1]),
                        (Number) row[2]
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<ScoreFrequency> findScoreFrequency() {
        List<Object[]> rawResults = dataRepository.findScoreFrequency();
        return rawResults.stream()
                .map(row -> new ScoreFrequency(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<SentimentCloud> findSentimentCloud() {
        List<Object[]> rawResults = dataRepository.findSentimentCloud();
        return rawResults.stream()
                .map(row -> new SentimentCloud(
                        String.valueOf(row[0]),
                        (String) row [1],
                        ((Number) row[2]).intValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ByondReview> findPriorityReview(int offset) {
       return dataRepository.findPriorityReview(offset);
    }

    @Override
    public List<ByondReview> findPriorityReviewBySearch(int offset,String keyword) {
        return dataRepository.findPriorityReviewBySearch(offset,keyword);
    }
}
