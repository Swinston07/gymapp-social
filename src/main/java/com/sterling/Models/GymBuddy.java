package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GymBuddy {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("buddy_id")
    private int buddyId;
    @JsonProperty("created_at")
    private Timestamp createdAt;

    
    public GymBuddy() {
    }

    public GymBuddy(int userId, int buddyId, Timestamp createdAt) {
        this.userId = userId;
        this.buddyId = buddyId;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBuddyId() {
        return buddyId;
    }

    public void setBuddyId(int buddyId) {
        this.buddyId = buddyId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
