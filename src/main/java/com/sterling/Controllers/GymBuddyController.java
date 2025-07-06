package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.GymBuddy;
import com.sterling.Services.GymBuddyService;

import io.javalin.http.Context;

public class GymBuddyController {
    private GymBuddyService gymBuddyService;

    public GymBuddyController(GymBuddyService gymBuddyService){
        this.gymBuddyService = gymBuddyService;
    }

    public void addGymBuddy(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int buddyId = Integer.parseInt(ctx.pathParam("buddyId"));

        if(requesterId != userId){
            ctx.status(403).result("Unauthorized");
            return;
        }

        gymBuddyService.addGymBuddy(userId, buddyId);
        gymBuddyService.addGymBuddy(buddyId, userId);

        ctx.status(201).result("Gym Buddy Added");
    }

    public void exists(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int buddyId = Integer.parseInt(ctx.pathParam("buddyId"));

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized");
            return;
        }

        boolean exists = gymBuddyService.exists(userId, buddyId);
        ctx.json(exists);
    }

    public void getGymBuddiesByUserId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));

        if(requesterId != userId){
            ctx.status(403).result("Unauthorized to view gym buddies for another user");
            return;
        }

        List<GymBuddy> buddies = gymBuddyService.getGymBuddiesByUserId(userId);
        ctx.status(200).json(buddies);
    }
}
