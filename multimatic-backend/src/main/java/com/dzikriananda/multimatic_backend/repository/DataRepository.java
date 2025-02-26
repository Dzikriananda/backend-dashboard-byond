package com.dzikriananda.multimatic_backend.repository;

import com.dzikriananda.multimatic_backend.model.ByondReview;
import com.dzikriananda.multimatic_backend.model.DaySentiment;
import com.dzikriananda.multimatic_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;

public interface DataRepository extends JpaRepository<ByondReview, Integer> {

    @Query(value = "SELECT * FROM byond_review", nativeQuery = true)
    List<ByondReview> findAllData();

    @Query(value = "SELECT\n" +
            "    DATE(at) AS review_date,  -- Alias for clarity\n" +
            "    sentiment,\n" +
            "    COUNT(*) AS sentiment_count\n" +
            "FROM\n" +
            "    byond_review\n" +
            "GROUP BY\n" +
            "    DATE(at), sentiment\n" +
            "ORDER BY\n" +
            "    review_date; ", nativeQuery = true)
    List<Object[]> findAllSentiment();
    @Query(value = "SELECT app_version ,sentiment, COUNT(*) AS count\n" +
            "\tFROM byond_review\n" +
            "\tGROUP BY sentiment,\"app_version\"",nativeQuery = true)
    List<Object[]> findDistributionSentiment();

    @Query(value = "SELECT score, COUNT(*) AS frequency\n" +
            "FROM byond_review br \n" +
            "GROUP BY score\n" +
            "ORDER BY score",nativeQuery = true)
    List<Object[]> findScoreFrequency();

}
