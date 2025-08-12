package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    private long id;
    @JsonProperty("user_id") private int userId;
    private String type;
    private String title;
    private String body;
    @JsonProperty("data_json") private String dataJson; // store raw JSON string
    @JsonProperty("created_at") private Timestamp createdAt;
    @JsonProperty("read_at") private Timestamp readAt;

    public Notification() {}
    public Notification(long id, int userId, String type, String title, String body, String dataJson,
                        Timestamp createdAt, Timestamp readAt) {
        this.id = id; this.userId = userId; this.type = type; this.title = title; this.body = body;
        this.dataJson = dataJson; this.createdAt = createdAt; this.readAt = readAt;
    }
    // getters/setters...
    public long getId(){ return id; }
    public void setId(long id){ this.id = id; }
    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId; }
    public String getType(){ return type; }
    public void setType(String type){ this.type = type; }
    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }
    public String getBody(){ return body; }
    public void setBody(String body){ this.body = body; }
    public String getDataJson(){ return dataJson; }
    public void setDataJson(String dataJson){ this.dataJson = dataJson; }
    public Timestamp getCreatedAt(){ return createdAt; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
    public Timestamp getReadAt(){ return readAt; }
    public void setReadAt(Timestamp readAt){ this.readAt = readAt; }
}
