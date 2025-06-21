package com.sterling.Connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBConnection {
    private static  final String URL = System.getenv("GYM_APP_URL");
    private static final String USER = System.getenv("USER");
    private static final String PW = System.getenv("PW");

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PW);
    }
}
