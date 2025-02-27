package com.dzikriananda.multimatic_backend.interfaces;

import com.dzikriananda.multimatic_backend.model.*;

import java.util.List;

public interface DataService {

    List<ByondReview>  findAllReview();
    List<DaySentiment> findAllSentiment();
    List<SentimentDistribution> findSentimentDistribution();

    List<ScoreFrequency> findScoreFrequency();
    List<SentimentCloud> findSentimentCloud();
    List<ByondReview> findPriorityReview(int offset);
    List<ByondReview> findPriorityReviewBySearch(int offset,String keyword);




}
