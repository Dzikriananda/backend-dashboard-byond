package com.dzikriananda.multimatic_backend.interfaces;

import com.dzikriananda.multimatic_backend.model.*;
import jakarta.annotation.Nullable;

import java.util.List;

public interface DataService {

    List<ByondReview> findAllReview();

    List<DaySentiment> findAllSentiment(@Nullable String startDate, @Nullable String endDate);

    List<SentimentDistribution> findSentimentDistribution(@Nullable String startDate, @Nullable String endDate);

    List<ScoreFrequency> findScoreFrequency(@Nullable String startDate, @Nullable String endDate);

    List<SentimentCloud> findSentimentCloud(@Nullable String startDate, @Nullable String endDate);

    PaginatedResponse<ByondReview> findPriorityReview(int offset, @Nullable String startDate, @Nullable String endDate);

    PaginatedResponse<ByondReview> findPriorityReviewBySearch(int offset, String keyword, @Nullable String startDate, @Nullable String endDate);

    LatestDate findLatestReviewDate();
}
