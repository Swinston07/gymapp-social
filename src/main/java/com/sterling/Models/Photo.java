package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photo {
    @JsonProperty("photo_id")
    private int photoId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("uploaded_at")
    private Timestamp uploadedAt;
    
    public Photo() {
    }

    public Photo(int photoId, int userId, String imageUrl, Timestamp uploadedAt) {
        this.photoId = photoId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.uploadedAt = uploadedAt;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    
}
