package com.sterling.Config;

import com.sterling.Utils.JwtUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

public class SecurityConfig {

    public static void applySecurity(Javalin app) {
        // ---- CORS ----
        app.before(ctx -> {
            // You can scope this to your web origins in production instead of "*"
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        // Preflight
        app.options("/*", ctx -> ctx.status(204));

        // ---- Protected routes ----
        // (keep this list in sync with your API surface)
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

    private static void protectRoute(Context ctx) {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ctx.status(401).result("Missing or invalid token");
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            int userId = JwtUtil.validateTokenAndGetUserId(token);
            // Attach to context so handlers can use it without re-parsing
            ctx.attribute("userId", userId);
        } catch (ExpiredJwtException e) {
            // Distinguish expired for better client UX (e.g., prompt re-login or refresh)
            ctx.status(401).result("Expired Token");
        } catch (JwtException e) {
            // Signature/malformed/etc.
            ctx.status(401).result("Invalid Token");
        } catch (Exception e) {
            // Any other unexpected failure
            ctx.status(401).result("Auth Error");
        }
    }
}
