package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignedWorkout {
    @JsonProperty("assignment_id")
    private int assignmentId;
    @JsonProperty("trainer_id")
    private int trainerId;
    @JsonProperty("client_id")
    private int clientId;
    @JsonProperty("workout_name")
    private String workoutName;
    private String description;
    @JsonProperty("date_assigned")
    private Timestamp dateAssigned;
    @JsonProperty("is_completed")
    private Boolean isCompleted;
    @JsonProperty("completed_on")
    private Timestamp completedOn;

    public AssignedWorkout() {
    }



    public AssignedWorkout(int assignmentId, int trainerId, int clientId, String workoutName, String description,
            Timestamp dateAssigned, Boolean isCompleted, Timestamp completedOn) {
        this.assignmentId = assignmentId;
        this.trainerId = trainerId;
        this.clientId = clientId;
        this.workoutName = workoutName;
        this.description = description;
        this.dateAssigned = dateAssigned;
        this.isCompleted = isCompleted;
        this.completedOn = completedOn;
    }

    public int getAssignmentId() {
        return assignmentId;
    }



    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }



    public int getTrainerId() {
        return trainerId;
    }



    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }



    public int getClientId() {
        return clientId;
    }



    public void setClientId(int clientId) {
        this.clientId = clientId;
    }



    public String getWorkoutName() {
        return workoutName;
    }



    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }



    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }



    public Timestamp getDateAssigned() {
        return dateAssigned;
    }



    public void setDateAssigned(Timestamp dateAssigned) {
        this.dateAssigned = dateAssigned;
    }



    public Boolean getIsCompleted() {
        return isCompleted;
    }



    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }



    public Timestamp getCompletedOn() {
        return completedOn;
    }



    public void setCompletedOn(Timestamp completedOn) {
        this.completedOn = completedOn;
    }

        
}
