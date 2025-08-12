package com.sterling.Controllers;

import java.util.List;
import java.util.Map;

import com.sterling.Models.Notification;
import com.sterling.Services.NotificationService;

import io.javalin.http.Context;

public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    // GET /users/:id/notifications?limit=20&offset=0
    public void list(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if (requesterId != userId) { ctx.status(403).result("Unauthorized"); return; }

        int limit = (ctx.queryParamAsClass("limit", Integer.class).getOrDefault(20));
        int offset = (ctx.queryParamAsClass("offset", Integer.class).getOrDefault(0));

        List<Notification> list = notificationService.getForUser(userId, limit, offset);
        ctx.json(list);
    }

    // POST /users/:id/notifications/:notificationId/read
    public void markRead(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if (requesterId != userId) { ctx.status(403).result("Unauthorized"); return; }

        long notificationId = Long.parseLong(ctx.pathParam("notificationId"));
        notificationService.markRead(userId, notificationId);
        ctx.status(200).result("Marked read");
    }

    // POST /users/:id/notifications/mark-all-read
    public void markAllRead(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if (requesterId != userId) { ctx.status(403).result("Unauthorized"); return; }

        notificationService.markAllRead(userId);
        ctx.status(200).result("All read");
    }

    // (Optional) POST /users/:id/notifications/test
    public void testSend(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if (requesterId != userId) { ctx.status(403).result("Unauthorized"); return; }
        Map<String,Object> body = ctx.bodyAsClass(Map.class);
        String title = (String) body.getOrDefault("title", "Test");
        String message = (String) body.getOrDefault("body", "Hello!");
        notificationService.notifyUser(userId, "TEST", title, message, body);
        ctx.status(201).result("Sent");
    }
}
