/*
 * -------------------------------------------------------------
 * Author: Sterling Winston
 * Date: Febuary 17, 2025
 * 
 * Description:
 * This program is part of the Gym Tracker Application.
 * It manages user registration, login, onboarding, workout tracking,
 * gym buddy matching, and profile customization features.
 *
 * All rights reserved. This source code is the intellectual property
 * of Sterling Winston. Unauthorized use, reproduction, or distribution
 * is prohibited without written permission.
 * -------------------------------------------------------------
 */

package com.sterling;

import com.sterling.Config.AppConfig;
import com.sterling.Config.RouteConfig;
import com.sterling.Config.SecurityConfig;
import com.sterling.Config.WebSocketConfig;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static final Map<String, WsContext> userSessions = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        // Create and configure app (explicit UTF-8)
        Javalin app = Javalin.create(cfg -> {
            cfg.http.defaultContentType = "application/json; charset=UTF-8";
        });

        // Force UTF-8 for inbound/outbound (defensive)
        app.before(ctx -> {
            try { ctx.req().setCharacterEncoding("UTF-8"); } catch (Exception ignored) {}
        });
        app.after(ctx -> {
            ctx.res().setCharacterEncoding("UTF-8");
            // keep content type explicit in case handlers set plain json
            if (ctx.res().getContentType() == null) {
                ctx.contentType("application/json; charset=UTF-8");
            }
        });

        // Apply security (CORS + protectRoute for JWT)
        SecurityConfig.applySecurity(app);

        // Register WebSocket handlers (for chat)
        WebSocketConfig.configureWebSocket(app, userSessions);

        // Load DI container and initialize routes
        Map<String, Object> beans = AppConfig.initializeDependencies();
        RouteConfig.registerRoutes(app, beans);

        // Start app
        int port = 7000;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            try { port = Integer.parseInt(envPort); } catch (NumberFormatException ignored) {}
        }

        System.out.println("Booting API with PORT env=" + System.getenv("PORT") + " -> using port=" + port);

        app.start(port);
    }
}
