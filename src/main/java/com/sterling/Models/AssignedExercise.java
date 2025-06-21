package com.sterling.Models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignedExercise {
    @JsonProperty("assigned_exercise_id")
    private int assignedExerciseId;
    @JsonProperty("assignment_id")
    private int assignmentId;
    @JsonProperty("exercise_name")
    private String exerciseName;
    private float weight;
    private int sets;
    private int reps;
    @JsonProperty("created_on")
    private Timestamp createdOn;
    @JsonProperty("is_completed")
    private boolean isCompleted;
    
    public AssignedExercise() {
    }

    public AssignedExercise(int assignedExerciseId, int assignmentId, String exerciseName, float weight, int sets,
            int reps, Timestamp createdOn, boolean isCompleted) {
        this.assignedExerciseId = assignedExerciseId;
        this.assignmentId = assignmentId;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.createdOn = createdOn;
        this.isCompleted = isCompleted;
    }

    public int getAssignedExerciseId() {
        return assignedExerciseId;
    }

    public void setAssignedExerciseId(int assignedExerciseId) {
        this.assignedExerciseId = assignedExerciseId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
