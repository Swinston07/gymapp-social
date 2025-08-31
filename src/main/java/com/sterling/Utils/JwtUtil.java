package com.sterling.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public final class JwtUtil {
    // ====== CONFIG ======
    // Access token lifetime (currently 24 hours). Adjust as needed during beta.
    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24; // 24 hour
    // Small tolerance for device/server clock differences.
    private static final long ALLOWED_SKEW_SECONDS = 60; // 60s

    private static final String SECRET;

    static {
        String s = System.getenv("JWT_SECRET");
        if (s == null || s.isBlank()) {
            try {
                var d = io.github.cdimascio.dotenv.Dotenv
                        .configure()
                        .ignoreIfMissing()
                        .load();
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

    private JwtUtil() {}

    private static SecretKey key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(int userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(EXPIRATION_MS)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the token signature, expiry, etc., and returns the user id (subject).
     * @throws ExpiredJwtException if the token is expired
     * @throws JwtException for any other parsing/signature errors
     */
    public static int validateTokenAndGetUserId(String token)
            throws ExpiredJwtException, JwtException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .setAllowedClockSkewSeconds(ALLOWED_SKEW_SECONDS)
                .build()
                .parseClaimsJws(token);

        return Integer.parseInt(claims.getBody().getSubject());
    }
}
