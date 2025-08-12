// src/main/java/com/sterling/Controllers/UnreadController.java
package com.sterling.Controllers;

import com.sterling.Services.UnreadService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnreadController {

    private static final Set<String> ALLOWED_SECTIONS = Set.of(
        "buddies", "invites", "sessions", "reviews", "messages"
    );

    private final UnreadService unreadService;

    public UnreadController(UnreadService unreadService) {
        this.unreadService = unreadService;
    }

    // GET /users/:id/unread-summary
    public void summary(Context ctx) {
        Integer requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));

        if (requesterId == null || requesterId != userId) {
            ctx.status(403).result("Unauthorized");
            return;
        }

        Map<String, Integer> summary = unreadService.getSummary(userId);
        ctx.json(summary);
    }

    // POST /users/:id/sections/:section/seen
    public void markSectionSeen(Context ctx) {
        Integer requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        String section = ctx.pathParam("section").toLowerCase();

        if (requesterId == null || requesterId != userId) {
            ctx.status(403).result("Unauthorized");
            return;
        }

        if (!ALLOWED_SECTIONS.contains(section)) {
            ctx.status(400).result("Invalid section");
            return;
        }

        unreadService.markSectionSeen(userId, section);
        ctx.status(204); // No Content
    }

    // POST /users/:id/messages/read/:otherUserId
    public void markMessagesRead(Context ctx) {
        Integer requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int otherUserId = Integer.parseInt(ctx.pathParam("otherUserId"));

        if (requesterId == null || requesterId != userId) {
            ctx.status(403).result("Unauthorized");
            return;
        }

        unreadService.markMessagesRead(userId, otherUserId);
        ctx.status(204); // No Content
    }

    // in UnreadController.java
    public void unreadByPartner(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if (requesterId != userId) { ctx.status(403).result("Unauthorized"); return; }

        List<Map<String, Integer>> list = unreadService.getUnreadByPartner(userId);
        ctx.json(list);
    }
}