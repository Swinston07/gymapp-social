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
    @JsonProperty("user1_first_name")
    private String user1FirstName;
    @JsonProperty("user1_last_name")
    private String user1LastName;
    @JsonProperty("user2_first_name")
    private String user2FirstName;
    @JsonProperty("user2_last_name")
    private String user2LastName;
    
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

    public WorkoutSession(int sessionId, int user1Id, int user2Id, Timestamp scheduledTime, WorkoutStatus status,
            Timestamp createdAt, String user1FirstName, String user1LastName, String user2FirstName,
            String user2LastName) {
        this.sessionId = sessionId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.createdAt = createdAt;
        this.user1FirstName = user1FirstName;
        this.user1LastName = user1LastName;
        this.user2FirstName = user2FirstName;
        this.user2LastName = user2LastName;
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

    public void setScheduledTime(Timestamp scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getUser1FirstName() {
        return user1FirstName;
    }

    public void setUser1FirstName(String user1FirstName) {
        this.user1FirstName = user1FirstName;
    }

    public String getUser1LastName() {
        return user1LastName;
    }

    public void setUser1LastName(String user1LastName) {
        this.user1LastName = user1LastName;
    }

    public String getUser2FirstName() {
        return user2FirstName;
    }

    public void setUser2FirstName(String user2FirstName) {
        this.user2FirstName = user2FirstName;
    }

    public String getUser2LastName() {
        return user2LastName;
    }

    public void setUser2LastName(String user2LastName) {
        this.user2LastName = user2LastName;
    }
}
