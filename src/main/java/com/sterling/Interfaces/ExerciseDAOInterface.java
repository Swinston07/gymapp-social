package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.Exercise;

public interface ExerciseDAOInterface {
    void addExercise(Exercise exercise);
    Exercise getExerciseById(int id);
    List<Exercise> getExercisesByUserId(int userId);
    List<Exercise> getAllExercises();
    boolean updateExercise(Exercise exercise);
    boolean deleteExercise(int id);
}
