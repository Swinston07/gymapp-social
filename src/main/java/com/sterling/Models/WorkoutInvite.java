package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkoutInvite {
    private int id;
    @JsonProperty("sender_id")
    private int senderId;
    @JsonProperty("recipient_id")
    private int recipientId;
    private String status;
    private String message;
    @JsonProperty("sent_at")
    private Timestamp sentAt;
    @JsonProperty("responded_at")
    private Timestamp respondedAt;


    public WorkoutInvite(){
    }
    public WorkoutInvite(int id, int senderId, int recipientId, String status, String message, Timestamp sentAt,
            Timestamp respondedAt) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.status = status;
        this.message = message;
        this.sentAt = sentAt;
        this.respondedAt = respondedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getSenderId() {
        return senderId;
    }


    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }


    public int getRecipientId() {
        return recipientId;
    }


    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public Timestamp getSentAt() {
        return sentAt;
    }


    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }


    public Timestamp getRespondedAt() {
        return respondedAt;
    }


    public void setRespondedAt(Timestamp respondedAt) {
        this.respondedAt = respondedAt;
    }

    
}
