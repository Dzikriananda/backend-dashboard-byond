package com.dzikriananda.multimatic_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreFrequency {
    private int score;
    private int frequency;

    public ScoreFrequency(int score, int frequency) {
        this.score = score;
        this.frequency = frequency;
    }
}
