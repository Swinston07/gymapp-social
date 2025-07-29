package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkoutSession {
    @JsonProperty("session_id")
    private int sessionId;
    @JsonProperty("user1_id")
    private int user1Id;
    @JsonProperty("user2_id")
    private int user2Id;
    @JsonProperty("scheduled_time")
    private Timestamp scheduledTime;
    private WorkoutStatus status;
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    public WorkoutSession() {
    }

    public WorkoutSession(int sessionId, int user1Id, int user2Id, Timestamp scheduledTime, WorkoutStatus status,
            Timestamp createdAt) {
        this.sessionId = sessionId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public Timestamp getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduedTime(Timestamp scheduedTime) {
        this.scheduledTime = scheduedTime;
    }

    public WorkoutStatus getStatus() {
        return status;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    
}
