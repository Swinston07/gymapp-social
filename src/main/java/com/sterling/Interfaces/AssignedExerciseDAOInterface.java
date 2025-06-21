package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.AssignedExercise;

public interface AssignedExerciseDAOInterface {
    void addAssignedExercise(AssignedExercise assignedExercise);
    List<AssignedExercise> getAssignedExercisesByAssignmentId(int assignmentId);
    boolean updateAssignedExercise(AssignedExercise assignedExercise);
    boolean deleteAssignedExercise(int assignmentExerciseId);
    AssignedExercise getAssignedExerciseById(int assignedExerciseId);
    boolean markExerciseCompleted(int assignedExerciseId);
}
