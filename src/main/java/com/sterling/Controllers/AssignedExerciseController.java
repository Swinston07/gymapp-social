package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.AssignedExercise;
import com.sterling.Models.AssignedWorkout;
import com.sterling.Models.User;
import com.sterling.Services.AssignedExerciseService;
import com.sterling.Services.AssignedWorkoutService;
import com.sterling.Services.UserService;

import io.javalin.http.Context;

public class AssignedExerciseController {
    AssignedExerciseService assignedExerciseService;
    UserService userService;
    AssignedWorkoutService assignedWorkoutService;

    public AssignedExerciseController(AssignedExerciseService assignedExerciseService, UserService userService,
                                    AssignedWorkoutService assignedWorkoutService){
        this.assignedExerciseService = assignedExerciseService;
        this.userService = userService;
        this.assignedWorkoutService = assignedWorkoutService;
    }

    public void addAssignedExercise(Context ctx){
        int requesterId = ctx.attribute("userId");
        int assignmentId = Integer.parseInt(ctx.pathParam("assignmentId"));
        AssignedExercise assignedExercise = ctx.bodyAsClass(AssignedExercise.class);

        assignedExercise.setAssignmentId(assignmentId);

        //validate input
        if(assignedExercise.getAssignmentId() == 0 || assignedExercise.getExerciseName() == null){
            ctx.status(400).result("Missing required fields");
            return;
        }

        //Get Requester and assignment info
        User requester = userService.getUserById(requesterId);
        AssignedWorkout workout = assignedWorkoutService.getWorkoutById(assignedExercise.getAssignmentId());

        if(requester == null || workout == null){
            ctx.status(404).result("Requester or assigned workout not found");
            return;
        }


        boolean isTrainerOwner = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getTrainerId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isAdmin || isTrainerOwner)){
            ctx.status(403).result("Unauthorized: Only the assigned trainer or admin can add exercises to this workout");
            return;
        }
        
        try{
            assignedExerciseService.addAssignedExercise(assignedExercise);
            ctx.status(201).result("Assigned exercise added successfully");
        } catch(Exception e){
            ctx.status(500).result("Failed to add assigned exercise");
        }
    }

    public void getAssignedExercisesByAssignmentId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int assignmentId = Integer.parseInt(ctx.pathParam("assignmentId"));

        AssignedWorkout workout = assignedWorkoutService.getWorkoutById(assignmentId);
        User requester = userService.getUserById(requesterId);

        if(workout == null || requester == null){
            ctx.status(404).result("Workout or user not found");
            return;
        }

        boolean isClient = "client".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getClientId();
        boolean isTrainer = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getTrainerId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isAdmin || isTrainer || isClient)){
            ctx.status(403).result("Unauthorized to view exercises for this assignment");
            return;
        }

        List<AssignedExercise> exercises = assignedExerciseService.getAssignedExercisesByAssignmentId(assignmentId);
        ctx.json(exercises);
    }

    public void updateAssignedExercise(Context ctx){
        int requesterId = ctx.attribute("userId");
        int assignedExerciseId = Integer.parseInt(ctx.pathParam("assignedExerciseId"));

        //Fetch existing exercise
        AssignedExercise existingExercise = assignedExerciseService.getAssignedExerciseById(assignedExerciseId);

        if(existingExercise == null){
            ctx.status(404).result("Assigned exercise not found");
            return;
        }

        // Fetch requester and linked assignment info
        User requester = userService.getUserById(requesterId);
        AssignedWorkout workout = assignedWorkoutService.getWorkoutById(existingExercise.getAssignmentId());

        if(requester == null || workout == null){
            ctx.status(404).result("Requester or associated workout not found");
            return;
        }

        boolean isTrainerOwner = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getTrainerId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isAdmin || isTrainerOwner)){
            ctx.status(403).result("Unauthorized to update this exercise");
            return;
        }

        //Merge changes from request body
        AssignedExercise updatedExercise = ctx.bodyAsClass(AssignedExercise.class);
        updatedExercise.setAssignedExerciseId(assignedExerciseId);
        updatedExercise.setAssignmentId(existingExercise.getAssignmentId());

        boolean success = assignedExerciseService.updateAssignedExercise(updatedExercise);
        
        if(success){
            ctx.status(200).result("Assigned exercise updated successfully");
        }
        else{
            ctx.status(500).result("Failed to update assigned exercise");
        }
    }

    public void deleteAssignedExercise(Context ctx){
        int requesterId = ctx.attribute("userId");
        int assignedExerciseId = Integer.parseInt(ctx.pathParam("assignedExerciseId"));

        //Fetch exercise to get its associated assignment
        AssignedExercise assignedExercise = assignedExerciseService.getAssignedExerciseById(assignedExerciseId);

        if(assignedExercise == null){
            ctx.status(404).result("Assigned exercise not found");
            return;
        }

        AssignedWorkout workout = assignedWorkoutService.getWorkoutById(assignedExercise.getAssignmentId());
        User requester = userService.getUserById(requesterId);

        if(workout == null || requester == null){
            ctx.status(404).result("Requester or associated workout not found");
            return;
        }

        boolean isTrainerOwner = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getTrainerId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isAdmin || isTrainerOwner)){
            ctx.status(403).result("Unauthorized to delete this assigned exercise");
            return;
        }

        boolean success = assignedExerciseService.deleteAssignedExercise(assignedExerciseId);

        if(success){
            ctx.status(200).result("Assigned exercise deleted successfully");
        } else{
            ctx.status(500).result("Failed to delete assigned exercise");
        }
    }

    public void markExerciseCompleted(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("assignedExerciseId"));
        boolean success = assignedExerciseService.markExerciseCompleted(id);

        if(success){
            ctx.status(200).result("Exercise marked as completed");
        }
        else {
            ctx.status(404).result("Exercise not found or updated");
        }
    }
}
