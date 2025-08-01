package com.sterling.Config;

import com.sterling.Utils.JwtUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SecurityConfig {

    public static void applySecurity(Javalin app) {
        // CORS Headers
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        app.options("/*", ctx -> ctx.status(204));

        // Protected routes (JWT auth)
        app.before("/users/*", SecurityConfig::protectRoute);
        app.before("/trainers/*", SecurityConfig::protectRoute);
        app.before("/exercises/*", SecurityConfig::protectRoute);
        app.before("/assigned-workouts/*", SecurityConfig::protectRoute);
        app.before("/assigned-exercises/*", SecurityConfig::protectRoute);
        app.before("/blog-posts/*", SecurityConfig::protectRoute);
        app.before("/users/*/blog-posts", SecurityConfig::protectRoute);
        app.before("/progress/*", SecurityConfig::protectRoute);
        app.before("/users/*/workout-invites*", SecurityConfig::protectRoute);
        app.before("/users/*/gym-buddies*", SecurityConfig::protectRoute);
        app.before("/messages/*", SecurityConfig::protectRoute);
        app.before("/photos/*", SecurityConfig::protectRoute);
        app.before("/sessions/*", SecurityConfig::protectRoute);
        app.before("/create-checkout-session", SecurityConfig::protectRoute);
        app.before("/create-billing-portal-session", SecurityConfig::protectRoute);
        app.before("/subscriptions/*", SecurityConfig::protectRoute);
        // Do NOT protect: /stripe/webhook
    }

    public static void protectRoute(Context ctx) {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ctx.status(401).result("Missing or invalid token");
            return;
        }

        String token = authHeader.replace("Bearer ", "").trim();
        try {
            int userId = JwtUtil.validateTokenAndGetUserId(token);
            ctx.attribute("userId", userId);
        } catch (Exception e) {
            ctx.status(401).result("Invalid Token");
        }
    }
}
