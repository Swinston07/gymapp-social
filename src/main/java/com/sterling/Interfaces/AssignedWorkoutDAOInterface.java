package com.sterling.Interfaces;

import java.sql.Timestamp;
import java.util.List;

import com.sterling.Models.AssignedWorkout;

public interface AssignedWorkoutDAOInterface {
    void assignWorkout(AssignedWorkout workout);
    List<AssignedWorkout> getWorkoutsByClientId(int clientId);
    List<AssignedWorkout> getWorkoutsByTrainerId(int trainerId);
    boolean markWorkoutCompleted(int assignmentId);
    AssignedWorkout getById(int assignmentId);
    boolean deleteAssignment(int assignmentId);
    public AssignedWorkout getWorkoutByClientIdAndDate(int clientId, Timestamp date);
}
