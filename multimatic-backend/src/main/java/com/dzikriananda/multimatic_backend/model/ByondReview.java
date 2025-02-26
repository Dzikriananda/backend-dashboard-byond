package com.dzikriananda.multimatic_backend.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

import java.time.ZonedDateTime;

@Entity
@Table(name = "byond_review")
@Getter
@Setter
public class ByondReview {

    @Id
    @Column(name = "review_id", nullable = false, unique = true)
    private String reviewId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_image")
    private String userImage;

    @Column(name = "content")
    private String content;

    @Column(name = "score")
    private Integer score;

    @Column(name = "at")
    private ZonedDateTime at;

    @Column(name = "sentiment")
    private String sentiment;

    @Column(name = "preprocessed_content")
    private String preprocessedContent;

    @Column(name = "thumbs_up_count")
    private Integer thumbsUpCount;

    @Column(name = "app_version")
    private String appVersion;
}
