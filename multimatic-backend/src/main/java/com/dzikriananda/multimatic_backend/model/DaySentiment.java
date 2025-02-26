package com.dzikriananda.multimatic_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DaySentiment {


    private LocalDate reviewDate;
    private String sentiment;
    private Integer sentimentCount;

    public DaySentiment(LocalDate reviewDate, String sentiment, Integer sentimentCount) {
        this.reviewDate = reviewDate;
        this.sentiment = sentiment;
        this.sentimentCount = sentimentCount;
    }


}
