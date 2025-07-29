package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.WorkoutSession;
import com.sterling.Models.WorkoutStatus;
import com.sterling.Services.WorkoutSessionService;

import io.javalin.http.Context;

public class WorkoutSessionController {
    WorkoutSessionService workoutSessionService;
    
    public WorkoutSessionController(WorkoutSessionService workoutSessionService) {
        this.workoutSessionService = workoutSessionService;
    }

    public void createSession(Context ctx) {
        try {
            WorkoutSession session = ctx.bodyAsClass(WorkoutSession.class);
            int requesterId = ctx.attribute("userId");
            
            if(session.getUser1Id() != requesterId && session.getUser2Id() != requesterId) {
                ctx.status(403).result("Not authorized to schedule this session");
                return;
            }

            WorkoutSession created = workoutSessionService.createSession(session);
            if(created != null) {
                ctx.status(201).json(created);
            } else {
                ctx.status(500).result("Failed to create session.");
            }
        } catch (Exception e) {
            ctx.status(400).result("Invalid input: "  + e.getMessage());
        }
    }

    public void getSessionById(Context ctx) {
        int sessionId = Integer.parseInt(ctx.pathParam("sessionId"));
        int requesterId = ctx.attribute("userId");
        
        WorkoutSession session = workoutSessionService.getSessionById(sessionId);

        if(session != null) {
            if(session.getUser1Id() != requesterId && session.getUser2Id() != requesterId) {
                ctx.status(403).result("Not authorized to view this session");
                return;
            }
            ctx.json(session);
        } else {
            ctx.status(404).result("Session not found");
        }
    }

    public void getSessionsByUserId(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        String statusParam = ctx.queryParam("status");

        List<WorkoutSession> sessions;

        if(requesterId != userId) {
            ctx.status(403).result("Not authorized to view sessions for another user");
            return;
        }

        if (statusParam != null){
            try {
                WorkoutStatus status = WorkoutStatus.valueOf(statusParam.toUpperCase());
                sessions = workoutSessionService.getSessionsByUserIdAndStatus(userId, status);
            } catch (IllegalArgumentException e) {
                ctx.status(400).result("Invalid status value");
                return;
            }
        } else {
            sessions = workoutSessionService.getSessionsByUserId(userId);
        }

        ctx.json(sessions);
    }

    public void updateSessionStatus(Context ctx) {
        int sessionId = Integer.parseInt(ctx.pathParam("sessionId"));
        String statusParam = ctx.pathParam("status");
        int requesterId = ctx.attribute("userId");

        try {
            WorkoutSession session = workoutSessionService.getSessionById(sessionId);

            if(session.getUser1Id() != requesterId && session.getUser2Id() != requesterId) {
                ctx.status(403).result("Not authorized to update this session's status");
                return;
            }

            WorkoutStatus status = WorkoutStatus.valueOf(statusParam.toUpperCase());
            boolean updated = workoutSessionService.updateSessionStatus(sessionId, status);

            if(updated) {
                ctx.status(200).result("Session status updated");
            } else {
                ctx.status(404).result("Session not found.");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid status value");
        }
    }

    public void deleteSession(Context ctx){
        int requesterId = ctx.attribute("userId");
        int sessionId = Integer.parseInt(ctx.pathParam("sessionId"));
        WorkoutSession session = workoutSessionService.getSessionById(sessionId);

        if(session == null ){
            ctx.status(400).result("Session not found");
            return;
        }

        if(session.getUser1Id() != requesterId && session.getUser2Id() != requesterId) {
            ctx.status(403).result("Not authorized to delete this session");
            return;
        }

        boolean deleted = workoutSessionService.deleteSession(sessionId);
        if(deleted) {
            ctx.status(200).result("Session deleted");
        } else {
            ctx.status(500).result("Failed to delete session.");
        }
    }
}
