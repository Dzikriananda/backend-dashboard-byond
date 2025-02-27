package com.dzikriananda.multimatic_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppDetail {

    @JsonProperty("App Downloads")
    private String appDownloads;

    @JsonProperty("App Score")
    private double appScore;

    @JsonProperty("Number of Reviews")
    private int numberOfReviews;

}
