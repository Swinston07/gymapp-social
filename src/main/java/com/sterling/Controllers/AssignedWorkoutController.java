package com.sterling.Controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.sterling.Models.AssignedWorkout;
import com.sterling.Models.User;
import com.sterling.Services.AssignedWorkoutService;
import com.sterling.Services.UserService;

import io.javalin.http.Context;

public class AssignedWorkoutController {
    private AssignedWorkoutService assignedWorkoutService;
    private UserService userService;

    public AssignedWorkoutController(AssignedWorkoutService assignedWorkoutService, UserService userService){
        this.assignedWorkoutService = assignedWorkoutService;
        this.userService = userService;
    }


    public void assignWorkout(Context ctx){
        int requesterId = ctx.attribute("userId");
        int trainerId = Integer.parseInt(ctx.pathParam("trainerId"));
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        AssignedWorkout workout = ctx.bodyAsClass(AssignedWorkout.class);

        //Validate requester role
        User requester = userService.getUserById(requesterId);
        User trainer = userService.getUserById(trainerId);
        User client = userService.getUserById(clientId);

        //assign Ids
        workout.setClientId(clientId);
        workout.setTrainerId(trainerId);

        if(requester == null || trainer == null || client == null){
            ctx.status(404).result("User not found");
            return;
        }

        //Validate roles
        boolean isAdmin = requester.getRole().equalsIgnoreCase("admin");
        boolean isTrainerSelf = requester.getRole().equalsIgnoreCase("trainer") && requesterId == trainerId;

        if(!(isAdmin || isTrainerSelf)){
            ctx.status(403).result("Unauthorized to assign workout");
            return;
        }

        if(requester==null || 
        (!"trainer".equalsIgnoreCase(requester.getRole())&&!"admin".equalsIgnoreCase(requester.getRole()))){
            ctx.status(403).result("Only trainers and admin can assign workouts");
            return;
        }

        //If trainer ensure they are assigning to their own client
        if("trainer".equalsIgnoreCase(requester.getRole()) && requesterId!=client.getTrainerId()){
            ctx.status(403).result("Trainers can only assign workouts on their own behalf.");
            return;
        }

        //Assign workout
        try{
            assignedWorkoutService.assignWorkout(workout);
            ctx.status(201).result("Workout assigned successfully");
        } catch(Exception e){
            e.printStackTrace();
            ctx.status(500).result("Server error assigning workout");
        }
    }

    public void getWorkoutsByClientId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));

        User requester = userService.getUserById(requesterId);
        User client = userService.getUserById(clientId);

        if(requester == null || client == null){
            ctx.status(404).result("User not found");
            return;
        }

        boolean isSelf = requesterId == clientId;
        boolean isTrainer = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == client.getTrainerId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isSelf || isTrainer || isAdmin)){
            ctx.status(403).result("Unauthorized to view client's workouts");
            return;
        }

        List<AssignedWorkout> workouts = assignedWorkoutService.getWorkoutsByClientId(clientId);
        ctx.json(workouts);
    }
    
    public void getWorkoutsByTrainerId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int trainerId = Integer.parseInt(ctx.pathParam("trainerId"));

        User requester = userService.getUserById(requesterId);
        User trainer = userService.getUserById(trainerId);

        if(requester == null || trainer == null){
            ctx.status(404).result("User not found");
            return;
        }

        boolean isSelf = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == trainerId;
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isSelf || isAdmin)){
            ctx.status(403).result("Unauthorized to view this trainer's assigned workouts");
            return;
        }

        List<AssignedWorkout> workouts = assignedWorkoutService.getWorkoutsByTrainerId(trainerId);
        ctx.json(workouts);
    }

    public void markWorkoutCompleted(Context ctx){
        int requesterId = ctx.attribute("userId");
        int assignmentId = Integer.parseInt(ctx.pathParam("assignmentId"));

        AssignedWorkout workout = assignedWorkoutService.getWorkoutById(assignmentId);
        User requester = userService.getUserById(requesterId);

        if(workout == null || requester == null){
            ctx.status(404).result("Workout or user not found");
            return;
        }

        boolean isClientSelf = "client".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getClientId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isClientSelf || isAdmin)){
            ctx.status(403).result("Unauthorized to complete this workout");
            return;
        }

        boolean success = assignedWorkoutService.markWorkoutCompleted(assignmentId);
        if(success){
            ctx.status(200).result("Workout marked as completed");
        }
        else{
            ctx.status(500).result("Failed to update workout");
        }
    }

    public void deleteAssignment(Context ctx){
        int requesterId = ctx.attribute("userId");
        int assignmentId = Integer.parseInt(ctx.pathParam("assignmentId"));

        AssignedWorkout workout = assignedWorkoutService.getWorkoutById(assignmentId);
        User requester = userService.getUserById(requesterId);

        if(workout == null || requester == null){
            ctx.status(404).result("Workout or user not found");
            return;
        }

        boolean isTrainerOwner = "trainer".equalsIgnoreCase(requester.getRole()) && requesterId == workout.getTrainerId();
        boolean isAdmin = "admin".equalsIgnoreCase(requester.getRole());

        if(!(isAdmin || isTrainerOwner)){
            ctx.status(404).result("Unauthorized to delete this workout assignment");
            return;
        }

        boolean deleted = assignedWorkoutService.deleteAssignment(assignmentId);

        if(deleted){
            ctx.status(200).result("Workout assignment deleted");
        }
        else{
            ctx.status(500).result("Failed to delte workout assignment");
        }
    }

    public void getWorkoutsByClientIdAndDate(Context ctx){
        int requesterId = ctx.attribute("userId");
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        String dateStr = ctx.pathParam("date").replace(" ", "T");

        if(requesterId != clientId){
            ctx.status(403).result("You are not authorized to access another user's workout.");
            return;
        }

        try{
            Timestamp dateTime = Timestamp.valueOf(LocalDateTime.parse(dateStr));
            AssignedWorkout workout = assignedWorkoutService.getWorkoutByClientIdAndDate(requesterId, dateTime);

            if(workout != null) {
                ctx.json(workout);
            } else {
                ctx.status(404).result("No workout assigned for this timestamp");    
            }

        } catch (DateTimeParseException e){
            ctx.status(400).result("Invalid Time Stamp format");
        } catch (Exception e) {
            ctx.status(500).result("Server error: " + e.getMessage());
        }
    }
}
