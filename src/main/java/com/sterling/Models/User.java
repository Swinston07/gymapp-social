package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private int id;
    private String email;
    private String username;
    @JsonProperty("password_hash")
    private String password;

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

    int age;
    @JsonProperty("start_weight")
    float startWeight;
    @JsonProperty("start_body_fat_percentage")
    float startBodyFatPercentage;
    @JsonProperty("feet")
    int heightFeet;
    @JsonProperty("inches")
    int heightInches;
    @JsonProperty("current_weight")
    float currentWeight;
    @JsonProperty("current_body_fat_percentage")
    float currentBodyFatPercentage;
    @JsonProperty("created_on")
    Timestamp createdOn;
    private String role;
    @JsonProperty("trainer_id")
    private Integer trainerId;
    @JsonProperty("home_gym")
    private String homeGym;
    private Double latitude;
    private Double longitude;

    public User() {
    }

    public User(int id, String email, String username, String password, String firstName, String lastName, int age,
            float startWeight, float startBodyFatPercentage, int heightFeet, int heightInches, float currentWeight,
            float currentBodyFatPercentage, Timestamp createdOn, String role, Integer trainerId, String homeGym,
            Double latitude, Double longitude) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.startWeight = startWeight;
        this.startBodyFatPercentage = startBodyFatPercentage;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.currentWeight = currentWeight;
        this.currentBodyFatPercentage = currentBodyFatPercentage;
        this.createdOn = createdOn;
        this.role = role;
        this.trainerId = trainerId;
        this.homeGym = homeGym;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(float startWeight) {
        this.startWeight = startWeight;
    }

    public float getStartBodyFatPercentage() {
        return startBodyFatPercentage;
    }

    public void setStartBodyFatPercentage(float startBodyFatPercentage) {
        this.startBodyFatPercentage = startBodyFatPercentage;
    }

    public int getHeightFeet() {
        return heightFeet;
    }

    public void setHeightFeet(int heightFeet) {
        this.heightFeet = heightFeet;
    }

    public int getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(int heightInches) {
        this.heightInches = heightInches;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public float getCurrentBodyFatPercentage() {
        return currentBodyFatPercentage;
    }

    public void setCurrentBodyFatPercentage(float currentBodyFatPercentage) {
        this.currentBodyFatPercentage = currentBodyFatPercentage;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public String getHomeGym() {
        return homeGym;
    }

    public void setHomeGym(String homeGym) {
        this.homeGym = homeGym;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
