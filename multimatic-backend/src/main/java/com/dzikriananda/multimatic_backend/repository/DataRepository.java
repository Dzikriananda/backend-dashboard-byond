package com.dzikriananda.multimatic_backend.repository;

import com.dzikriananda.multimatic_backend.model.ByondReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataRepository extends JpaRepository<ByondReview, Integer> {

    @Query(value = "SELECT * FROM byond_review", nativeQuery = true)
    List<ByondReview> findAllData();

    @Query(value = """
            SELECT DATE(at) AS review_date, sentiment, COUNT(*) AS sentiment_count 
            FROM byond_review 
            WHERE (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE)) 
            GROUP BY DATE(at), sentiment 
            ORDER BY review_date
            """, nativeQuery = true)
    List<Object[]> findAllSentiment(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = """
            SELECT app_version, sentiment, COUNT(*) AS count
            FROM byond_review
            WHERE (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
            GROUP BY sentiment, app_version
            """, nativeQuery = true)
    List<Object[]> findDistributionSentiment(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = """
            SELECT score, COUNT(*) AS frequency
            FROM byond_review
            WHERE (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
            GROUP BY score
            ORDER BY score
            """, nativeQuery = true)
    List<Object[]> findScoreFrequency(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = """
            WITH word_counts AS (
                SELECT 
                    sentiment,
                    lower(word) AS word, 
                    COUNT(*) AS frequency
                FROM (
                    SELECT sentiment, regexp_split_to_table(preprocessed_content, '\\s+') AS word
                    FROM byond_review
                    WHERE (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
                    AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
                ) AS words
                GROUP BY sentiment, word
                ORDER BY sentiment, frequency DESC
            )
            SELECT * FROM word_counts
            """, nativeQuery = true)
    List<Object[]> findSentimentCloud(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = """
            SELECT * FROM byond_review 
            WHERE (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
            ORDER BY thumbs_up_count desc 
            LIMIT 10 OFFSET :offset
            """, nativeQuery = true)
    List<ByondReview> findPriorityReview(
            @Param("offset") int offset,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = """
            SELECT * FROM byond_review 
            WHERE content ILIKE '%' || :keyword || '%'
            AND (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
            ORDER BY thumbs_up_count desc 
            LIMIT 10 OFFSET :offset
            """, nativeQuery = true)
    List<ByondReview> findPriorityReviewBySearch(
            @Param("offset") int offset,
            @Param("keyword") String keyword,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = "SELECT at FROM byond_review ORDER BY at DESC LIMIT 1", nativeQuery = true)
    List<Object[]> findLatestReviewDate();

    @Query(value = """
            SELECT count(*) FROM byond_review 
            WHERE(:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
            """, nativeQuery = true)
    List<Object[]> findTotalRow(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @Query(value = """
            SELECT count(*) FROM byond_review 
            WHERE content ILIKE '%' || :keyword || '%'
            AND (:startDate IS NULL OR DATE(at) >= CAST(:startDate AS DATE)) 
            AND (:endDate IS NULL OR DATE(at) <= CAST(:endDate AS DATE))
            """, nativeQuery = true)
    List<Object[]> findTotalRowBySearch(
            @Param("keyword") String keyword,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);
}
