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

        // Create and configure app
        Javalin app = Javalin.create();

        // Apply security (CORS + protectRoute for JWT)
        SecurityConfig.applySecurity(app);

        // Register WebSocket handlers (for chat)
        WebSocketConfig.configureWebSocket(app, userSessions);

        // Load DI container and initialize routes
        Map<String, Object> beans = AppConfig.initializeDependencies();
        RouteConfig.registerRoutes(app, beans);

        // Start app
        app.start(7000);
    }
}
