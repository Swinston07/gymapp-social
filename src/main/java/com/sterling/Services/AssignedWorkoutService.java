package com.sterling.Services;

import java.sql.Timestamp;
import java.util.List;

import com.sterling.Interfaces.AssignedWorkoutDAOInterface;
import com.sterling.Models.AssignedWorkout;

public class AssignedWorkoutService {
    private final AssignedWorkoutDAOInterface assignedWorkoutDAO;

    public AssignedWorkoutService(AssignedWorkoutDAOInterface assignedWorkoutDAO){
        this.assignedWorkoutDAO=assignedWorkoutDAO;
    }

    public void assignWorkout(AssignedWorkout workout){
        assignedWorkoutDAO.assignWorkout(workout);
    }

    public List<AssignedWorkout> getWorkoutsByClientId(int clientId){
        return assignedWorkoutDAO.getWorkoutsByClientId(clientId);
    }

    public List<AssignedWorkout> getWorkoutsByTrainerId(int trainerId){
        return assignedWorkoutDAO.getWorkoutsByTrainerId(trainerId);
    }

    public boolean markWorkoutCompleted(int assignmentId){
        return assignedWorkoutDAO.markWorkoutCompleted(assignmentId);
    }

    public AssignedWorkout getWorkoutById(int assignmentId){
        return assignedWorkoutDAO.getById(assignmentId);
    }

    public boolean deleteAssignment(int assignmentId){
        return assignedWorkoutDAO.deleteAssignment(assignmentId);
    }

    public AssignedWorkout getWorkoutByClientIdAndDate(int clientId, Timestamp date){
        return assignedWorkoutDAO.getWorkoutByClientIdAndDate(clientId, date);
    }
}
