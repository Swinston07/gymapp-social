package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("session_id")
    private int sessionId;
    @JsonProperty("reviewer_id")
    private int reviewerId;
    @JsonProperty("reviewed_id")
    private int reviewedId;
    private int rating;
    private String comment;
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    public Review() {
    }

    public Review(int reviewId, int sessionId, int reviewerId, int reviewedId, int rating, String comment,
            Timestamp createdAt) {
        this.reviewId = reviewId;
        this.sessionId = sessionId;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(int reviewedId) {
        this.reviewedId = reviewedId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
