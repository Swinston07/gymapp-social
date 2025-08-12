package com.sterling.Push;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExpoPushGateway implements PushGateway {
    private static final URI EXPO_ENDPOINT = URI.create("https://exp.host/--/api/v2/push/send");
    private static final int BATCH_SIZE = 90; // Expo recommends <= 100 per request

    private final HttpClient http;
    private final ObjectMapper mapper;

    public ExpoPushGateway() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_2)
                .build(),
             new ObjectMapper());
    }

    public ExpoPushGateway(HttpClient http, ObjectMapper mapper) {
        this.http = http;
        this.mapper = mapper;
    }

    @Override
    public void send(List<String> deviceTokens, String title, String body, Map<String, Object> data) {
        if (deviceTokens == null || deviceTokens.isEmpty()) return;

        // Chunk tokens to keep payloads reasonable
        for (int i = 0; i < deviceTokens.size(); i += BATCH_SIZE) {
            List<String> chunk = deviceTokens.subList(i, Math.min(i + BATCH_SIZE, deviceTokens.size()));
            postChunk(chunk, title, body, data);
        }
    }

    private void postChunk(List<String> tokens, String title, String body, Map<String, Object> data) {
        try {
            // Build Expo messages (one per token)
            List<Map<String, Object>> messages = new ArrayList<>(tokens.size());
            for (String token : tokens) {
                messages.add(Map.of(
                        "to", token,
                        "title", title,
                        "body", body,
                        "sound", "default",
                        "priority", "high",
                        // "channelId", "default", // (Android) optional if you use channels
                        "data", data == null ? Map.of() : data
                ));
            }

            String json = mapper.writeValueAsString(messages);
            HttpRequest req = HttpRequest.newBuilder(EXPO_ENDPOINT)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                System.err.println("[ExpoPush] Non-200: " + resp.statusCode() + " body=" + resp.body());
                return;
            }

            // Minimal response handling (logs any ticket errors)
            // Response example: { "data":[{"status":"ok","id":"..."}] } or {"data":[{"status":"error","message":"..."}]}
            try {
                Map<?, ?> res = mapper.readValue(resp.body(), Map.class);
                Object dataNode = res.get("data");
                if (dataNode instanceof List) {
                    for (Object item : (List<?>) dataNode) {
                        if (item instanceof Map) {
                            Object status = ((Map<?, ?>) item).get("status");
                            if ("error".equals(status)) {
                                System.err.println("[ExpoPush] Ticket error: " + item);
                            }
                        }
                    }
                }
            } catch (Exception parseEx) {
                System.err.println("[ExpoPush] Failed to parse response: " + parseEx.getMessage());
            }
        } catch (Exception e) {
            // Never throw—push should be best-effort
            e.printStackTrace();
        }
    }
}
