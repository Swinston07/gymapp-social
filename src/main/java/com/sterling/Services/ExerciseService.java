package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.ExerciseDAOInterface;
import com.sterling.Models.Exercise;

public class ExerciseService {
    private final ExerciseDAOInterface exerciseDAO;

    public ExerciseService(ExerciseDAOInterface exerciseDAO){
        this.exerciseDAO = exerciseDAO;
    }

    public void addExercise(Exercise exercise){
        exerciseDAO.addExercise(exercise);
    }

    public Exercise getExerciseById(int id){
        return exerciseDAO.getExerciseById(id);
    }

    public List<Exercise> getAllExercises(){
        return exerciseDAO.getAllExercises();
    }

    public List<Exercise> getExercisesByUserId(int userId){
        return exerciseDAO.getExercisesByUserId(userId);
    }

    public boolean updateExercise(Exercise exercise){
        return exerciseDAO.updateExercise(exercise);
    }

    public boolean deleteExercise(int id){
        return exerciseDAO.deleteExercise(id);
    }
}
