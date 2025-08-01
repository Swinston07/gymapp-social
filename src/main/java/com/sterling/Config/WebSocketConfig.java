package com.sterling.Config;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import org.cloudinary.json.JSONObject;

import java.util.Map;

public class WebSocketConfig {

    public static void configureWebSocket(Javalin app, Map<String, WsContext> userSessions) {

        app.ws("/messages/{userId}", ws -> {

            ws.onConnect(ctx -> {
                String userId = ctx.pathParam("userId");
                userSessions.put(userId, ctx);
                System.out.println("WebSocket connected: " + userId);
            });

            ws.onMessage(ctx -> {
                String senderId = ctx.pathParam("userId");
                String raw = ctx.message();

                try {
                    JSONObject json = new JSONObject(raw);
                    String recipientId = json.getString("to");
                    String message = json.getString("message");

                    WsContext recipientSession = userSessions.get(recipientId);
                    if (recipientSession != null && recipientSession.session.isOpen()) {
                        recipientSession.send(senderId + ": " + message);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid websocket format: " + raw);
                    ctx.send("Error: Invalid message format. Expected JSON with 'to' and 'message'.");
                }
            });

            ws.onClose(ctx -> {
                String userId = ctx.pathParam("userId");
                userSessions.remove(userId);
                System.out.println("WebSocket closed: " + userId);
            });
        });
    }
}
