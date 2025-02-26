package com.dzikriananda.multimatic_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SentimentDistribution {
    private String sentiment;
    private Number count;
    private String appVersion;

    public SentimentDistribution(String appVersion,String sentiment,Number count) {
        this.sentiment = sentiment;
        this.count = count;
        this.appVersion = appVersion;
    }
}
