package com.dzikriananda.multimatic_backend.service;

import com.dzikriananda.multimatic_backend.interfaces.DataService;
import com.dzikriananda.multimatic_backend.model.*;
import com.dzikriananda.multimatic_backend.repository.DataRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    public List<DaySentiment> findAllSentiment(@Nullable String startDate, @Nullable String endDate) {
        List<Object[]> rawResults = dataRepository.findAllSentiment(
                startDate != null ? startDate : null,
                endDate != null ? endDate : null
        );

        return rawResults.stream()
                .map(row -> new DaySentiment(
                        ((java.sql.Date) row[0]).toLocalDate(),  // Convert SQL Date to LocalDate
                        String.valueOf(row[1]),  // Sentiment
                        ((Number) row[2]).intValue()  // Convert COUNT(*) to Integer
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<SentimentDistribution> findSentimentDistribution(@Nullable String startDate, @Nullable String endDate) {
        List<Object[]> rawResults = dataRepository.findDistributionSentiment(startDate, endDate);
        return rawResults.stream()
                .map(row -> new SentimentDistribution(
                        row[0] != null ? (String) row[0] : "Unknown",
                        String.valueOf(row[1]),
                        (Number) row[2]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScoreFrequency> findScoreFrequency(@Nullable String startDate, @Nullable String endDate) {
        List<Object[]> rawResults = dataRepository.findScoreFrequency(startDate, endDate);
        return rawResults.stream()
                .map(row -> new ScoreFrequency(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<SentimentCloud> findSentimentCloud(@Nullable String startDate, @Nullable String endDate) {
        List<Object[]> rawResults = dataRepository.findSentimentCloud(startDate, endDate);
        return rawResults.stream()
                .map(row -> new SentimentCloud(
                        String.valueOf(row[0]),
                        (String) row[1],
                        ((Number) row[2]).intValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PaginatedResponse<ByondReview> findPriorityReview(int offset, @Nullable String startDate, @Nullable String endDate) {
        List<ByondReview> data = dataRepository.findPriorityReview(offset, startDate, endDate);
        List<Object[]> rawResults = dataRepository.findTotalRow(startDate,endDate);
        Long pageSize = (Long) rawResults.get(0)[0];
        PaginatedResponse<ByondReview> response = new PaginatedResponse<ByondReview>(data,pageSize,offset);
        return response;
    }

    @Override
    public PaginatedResponse<ByondReview> findPriorityReviewBySearch(int offset, String keyword, @Nullable String startDate, @Nullable String endDate) {
        List<ByondReview> data = dataRepository.findPriorityReviewBySearch(offset, keyword, startDate, endDate);
        List<Object[]> rawResults = dataRepository.findTotalRowBySearch(keyword, startDate, endDate);
        Long pageSize = (Long) rawResults.get(0)[0];
        PaginatedResponse<ByondReview> response = new PaginatedResponse<ByondReview>(data,pageSize,offset);
        return response;
    }

    @Override
    public LatestDate findLatestReviewDate() {
        List<Object[]> rawResults = dataRepository.findLatestReviewDate();
        List<LatestDate> date = rawResults.stream().map(
                index -> new LatestDate((Timestamp) index[0])
        ).collect(Collectors.toList());
        return date.get(0);
    }
}
