package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceToken {
    private int id;
    @JsonProperty("user_id")
    private int userId;
    private String token;          // e.g., ExponentPushToken[...]
    private String platform;       // ios | android | web
    @JsonProperty("created_at")
    private Timestamp createdAt;
    @JsonProperty("revoked_at")
    private Timestamp revokedAt;

    public DeviceToken() {}
    public DeviceToken(int id, int userId, String token, String platform, Timestamp createdAt, Timestamp revokedAt) {
        this.id = id; this.userId = userId; this.token = token; this.platform = platform;
        this.createdAt = createdAt; this.revokedAt = revokedAt;
    }

    // getters/setters...
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId; }
    public String getToken(){ return token; }
    public void setToken(String token){ this.token = token; }
    public String getPlatform(){ return platform; }
    public void setPlatform(String platform){ this.platform = platform; }
    public Timestamp getCreatedAt(){ return createdAt; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
    public Timestamp getRevokedAt(){ return revokedAt; }
    public void setRevokedAt(Timestamp revokedAt){ this.revokedAt = revokedAt; }
}
