package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.AssignedExerciseDAOInterface;
import com.sterling.Models.AssignedExercise;

public class AssignedExerciseService {
    private final AssignedExerciseDAOInterface assignedExerciseDAO;

    public AssignedExerciseService(AssignedExerciseDAOInterface assignedExerciseDAO){
        this.assignedExerciseDAO = assignedExerciseDAO;
    }

    public void addAssignedExercise(AssignedExercise assignedExercise){
        assignedExerciseDAO.addAssignedExercise(assignedExercise);
    }

    public List<AssignedExercise> getAssignedExercisesByAssignmentId(int assignmentId){
        return assignedExerciseDAO.getAssignedExercisesByAssignmentId(assignmentId);
    }

    public boolean updateAssignedExercise(AssignedExercise assignedExercise){
        return assignedExerciseDAO.updateAssignedExercise(assignedExercise);
    }

    public boolean deleteAssignedExercise(int assignmentExerciseId){
        return assignedExerciseDAO.deleteAssignedExercise(assignmentExerciseId);
    }

    public AssignedExercise getAssignedExerciseById(int assignedExerciseId){
        return assignedExerciseDAO.getAssignedExerciseById(assignedExerciseId);
    }

    public boolean markExerciseCompleted(int assignedExerciseId){
        return assignedExerciseDAO.markExerciseCompleted(assignedExerciseId);
    }
}
