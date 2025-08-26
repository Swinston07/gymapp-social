package com.sterling.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public final class JwtUtil {
    private static final String SECRET;
    private static final long   EXPIRATION_MS = 1000L * 60 * 60; // 1 hour (adjust as needed)

    static {
        String s = System.getenv("JWT_SECRET");
        if (s == null || s.isBlank()) {
            try {
                var d = io.github.cdimascio.dotenv.Dotenv.configure().ignoreIfMissing().load();
                s = d.get("JWT_SECRET");
            } catch (Exception ignored) {}
        }
        if (s == null || s.isBlank()) {
            throw new IllegalStateException("JWT_SECRET is not set (env or .env).");
        }
        if (s.length() < 32) { // HS256 needs >= 256-bit key
            throw new IllegalStateException("JWT_SECRET must be at least 32 characters.");
        }
        SECRET = s;
    }

    private static SecretKey key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(int userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(EXPIRATION_MS)))
                .signWith(key())
                .compact();
    }


    public static int validateTokenAndGetUserId(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
        return Integer.parseInt(claims.getBody().getSubject());
    }
}
