package com.sterling.Controllers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.sterling.Models.WorkoutInvite;
import com.sterling.Services.WorkoutInviteService;

import io.javalin.http.Context;

public class WorkoutInviteController {
    private WorkoutInviteService workoutInviteService;

    public WorkoutInviteController(WorkoutInviteService workoutInviteService){
        this.workoutInviteService = workoutInviteService;
    }

    public void sendInvite(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int recipientId = Integer.parseInt(ctx.pathParam("recipientId"));
        WorkoutInvite invite = ctx.bodyAsClass(WorkoutInvite.class);

        if(requesterId != userId){
            ctx.status(403).result("Unauthorized to send an invite for another user");
            return;
        }

        invite.setSenderId(userId);
        invite.setRecipientId(recipientId);
        invite.setSentAt(Timestamp.from(Instant.now()));
        invite.setStatus("pending");
        
        String result = workoutInviteService.sendInvite(invite);
        ctx.status(201).result(result);
    }

    public void getInvitesForUser(Context ctx){
        int requesterId =ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to view another user's workout invites");
            return;
        }

        List<WorkoutInvite> invites = workoutInviteService.getInviteForUser(userId);
        ctx.status(200).json(invites);
    }

    public void updateInviteStatus(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int inviteId = Integer.parseInt(ctx.pathParam("inviteId"));
        Map<String, String> body = ctx.bodyAsClass(Map.class);

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to update workout invite for another user");
            return;
        }

        String status = body.get("status");
        Timestamp respondedAt = Timestamp.from(Instant.now());
        
        boolean success = workoutInviteService.updateInviteStatus(inviteId, status, respondedAt);

        if(success){
            ctx.status(200).result("Invite status updated");
        }
        else{
            ctx.status(404).result("Invite not found");
        }
    }
}
