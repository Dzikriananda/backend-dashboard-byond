package com.dzikriananda.multimatic_backend.interfaces;

import com.dzikriananda.multimatic_backend.model.ByondReview;
import com.dzikriananda.multimatic_backend.model.DaySentiment;
import com.dzikriananda.multimatic_backend.model.SentimentDistribution;

import java.util.List;

public interface DataService {

    List<ByondReview>  findAllReview();
    List<DaySentiment> findAllSentiment();
    List<SentimentDistribution> findSentimentDistribution();



}
