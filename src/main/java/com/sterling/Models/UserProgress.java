package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProgress {
    private int id;
    @JsonProperty("user_id")
    private int userId;
    private float weight;
    @JsonProperty("body_fat_percentage")
    private float bodyFatPercentage;
    @JsonProperty("recorded_at")
    private Timestamp recordedAt;

    public UserProgress(){
    }

    public UserProgress(int id, int userId, float weight, float bodyFatPercentage, Timestamp recordedAt) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
        this.recordedAt = recordedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(float bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Timestamp getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Timestamp recordedAt) {
        this.recordedAt = recordedAt;
    }

    
}
