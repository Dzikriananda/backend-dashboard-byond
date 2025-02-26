package com.dzikriananda.multimatic_backend.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SentimentCloud {
    private String sentiment;
    private String word;
    private int frequency;

    public SentimentCloud(String sentiment, String word, int frequency) {
        this.sentiment = sentiment;
        this.word = word;
        this.frequency = frequency;

    }

}
