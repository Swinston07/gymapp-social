package com.sterling.Connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()   // ok on Railway (no .env there)
            .load();

    private static String firstNonBlank(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }

    // Read from .env first (local), then real env (Railway)
    private static final String HOST = firstNonBlank(DOTENV.get("DB_HOST"), System.getenv("DB_HOST"));
    private static final String PORT = firstNonBlank(DOTENV.get("DB_PORT"), System.getenv("DB_PORT"), "3306");
    private static final String DB   = firstNonBlank(DOTENV.get("DB_NAME"), System.getenv("DB_NAME"));
    private static final String USER = firstNonBlank(DOTENV.get("DB_USER"), System.getenv("DB_USER"));
    private static final String PW   = firstNonBlank(DOTENV.get("DB_PASSWORD"), System.getenv("DB_PASSWORD"));

    // Build JDBC URL from the actual values above
    private static final String URL = String.format(
        "jdbc:mysql://%s:%s/%s?sslMode=REQUIRED&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        HOST, PORT, DB
    );

    public static Connection getConnection() throws SQLException {
        if (HOST == null || DB == null || USER == null || PW == null) {
            throw new SQLException("Missing DB envs: require DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD.");
        }
        return DriverManager.getConnection(URL, USER, PW);
    }
}
