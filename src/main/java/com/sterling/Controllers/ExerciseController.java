package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.Exercise;
import com.sterling.Services.ExerciseService;

import io.javalin.http.Context;

public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService=exerciseService;
    }

    public void addExercise(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Exercise exercise = ctx.bodyAsClass(Exercise.class);
        exercise.setUserId(id);
        exerciseService.addExercise(exercise);
        ctx.status(201).json(exercise);
    }

    public void getExerciseById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Exercise exercise = exerciseService.getExerciseById(id);

        if(exercise!=null)
            ctx.json(exercise);
        else
        ctx.status(404).result("Exercise not found");
    }

    public void getAllExercises(Context ctx){
        List<Exercise> exercises = exerciseService.getAllExercises();
        ctx.json(exercises);
    }

    public void getExercisesByUserId(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        List<Exercise> exercises = exerciseService.getExercisesByUserId(id);
        ctx.json(exercises);
    }

    public void updateExercise(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Exercise exercise = ctx.bodyAsClass(Exercise.class);
        exercise.setId(id);
        boolean updated = exerciseService.updateExercise(exercise);

        if(updated)
            ctx.status(200).result("Exercise updated");
        else
            ctx.status(404).result("Exercise not found");
    }

    public void deleteExercise(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = exerciseService.deleteExercise(id);
        
        if(deleted)
            ctx.status(200).result("Exercise deleted");
        else
            ctx.status(404).result("Exercise not found");
    }
}
