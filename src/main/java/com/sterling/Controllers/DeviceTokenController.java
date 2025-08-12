package com.sterling.Controllers;

import java.util.List;
import java.util.Map;

import com.sterling.Services.DeviceTokenService;

import io.javalin.http.Context;

public class DeviceTokenController {
    private final DeviceTokenService deviceTokenService;

    public DeviceTokenController(DeviceTokenService deviceTokenService){
        this.deviceTokenService = deviceTokenService;
    }

    // POST /users/:id/devices   { "token": "...", "platform": "ios|android|web" }
    public void register(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if(requesterId != userId){
            ctx.status(403).result("Unauthorized");
            return;
        }
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String token = body.get("token");
        String platform = body.get("platform");

        try {
            deviceTokenService.registerToken(userId, token, platform);
            ctx.status(201).result("Device registered");
        } catch (IllegalArgumentException e){
            ctx.status(400).result(e.getMessage());
        }
    }

    // DELETE /users/:id/devices   { "token": "..." }
    public void revoke(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if(requesterId != userId){
            ctx.status(403).result("Unauthorized");
            return;
        }
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String token = body.get("token");
        deviceTokenService.revokeToken(userId, token);
        ctx.status(200).result("Device revoked");
    }

    // GET /users/:id/devices
    public void list(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        if(requesterId != userId){
            ctx.status(403).result("Unauthorized");
            return;
        }
        List<String> tokens = deviceTokenService.getActiveTokens(userId);
        ctx.json(tokens);
    }
}