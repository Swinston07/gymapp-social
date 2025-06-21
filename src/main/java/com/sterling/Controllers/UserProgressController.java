package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.UserProgress;
import com.sterling.Services.UserProgressService;

import io.javalin.http.Context;

public class UserProgressController {
    private final UserProgressService userProgressService;

    public UserProgressController(UserProgressService userProgressService){
        this.userProgressService = userProgressService;
    }

    public void addUserProgress(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        UserProgress progress = ctx.bodyAsClass(UserProgress.class);

        progress.setUserId(userId);

        if(userId != requesterId){
            ctx.status(403).result("You are not authorized to add progress for another user.");
            return;
        }

        userProgressService.addUserProgress(progress);
        ctx.status(201).result("Progress entry added");
    }

    public void getProgressByUserId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("userId"));

        if(requesterId != userId){
            ctx.status(403).result("You are not authorized to view another user's progress");
            return;
        }

        List<UserProgress> progressList = userProgressService.getProgressByUserId(userId);
        ctx.json(progressList); 
    }

    public void deleteProgressByUserId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("userId"));

        if(requesterId != userId){
            ctx.status(403).result("You are not authorized to delete another user's progress");
            return;
        }

        boolean success = userProgressService.deleteProgressByUserId(userId);

        if(success) {
            ctx.status(200).result("User progress deleted");
        } else {
            ctx.status(404).result("No progress records found for this user.");
        }
    }
}
