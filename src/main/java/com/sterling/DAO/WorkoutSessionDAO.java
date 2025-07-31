package com.sterling.DAO;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.WorkoutSessionDAOInterface;
import com.sterling.Models.WorkoutSession;
import com.sterling.Models.WorkoutStatus;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionDAO implements WorkoutSessionDAOInterface {

    @Override
    public WorkoutSession createSession(WorkoutSession session) {
        String sql = "INSERT INTO workout_sessions (user1_id, user2_id, scheduled_time, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, session.getUser1Id());
            stmt.setInt(2, session.getUser2Id());
            stmt.setTimestamp(3, session.getScheduledTime());
            stmt.setString(4, session.getStatus().toString());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    session.setSessionId(keys.getInt(1));
                }
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WorkoutSession getSessionById(int sessionId) {
        String sql = "SELECT * FROM workout_sessions WHERE session_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new WorkoutSession(
                        rs.getInt("session_id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("scheduled_time"),
                        WorkoutStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<WorkoutSession> getSessionsByUserId(int userId) {
        String sql = """
        SELECT 
        ws.*,
        u1.first_name AS user1_first_name, u1.last_name AS user1_last_name,
        u2.first_name AS user2_first_name, u2.last_name AS user2_last_name
         FROM workout_sessions ws
         JOIN users u1 ON ws.user1_id = u1.id
         JOIN users u2 ON ws.user2_id = u2.id 
         WHERE (ws.user1_id = ? OR ws.user2_id = ?)
         ORDER BY ws.scheduled_time ASC""";
        List<WorkoutSession> sessions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WorkoutSession session = new WorkoutSession(
                        rs.getInt("session_id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("scheduled_time"),
                        WorkoutStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at")
                );
                session.setUser1FirstName(rs.getString("user1_first_name"));
                session.setUser1LastName(rs.getString("user1_last_name"));
                session.setUser2FirstName(rs.getString("user2_first_name"));
                session.setUser2LastName(rs.getString("user2_last_name"));
                sessions.add(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    @Override
    public boolean updateSessionStatus(int sessionId, WorkoutStatus status) {
        String sql = "UPDATE workout_sessions SET status = ? WHERE session_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.toString());
            stmt.setInt(2, sessionId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteSession(int sessionId) {
        String sql = "DELETE FROM workout_sessions WHERE session_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sessionId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<WorkoutSession> getSessionsByUserIdAndStatus(int userId, WorkoutStatus status) {
        List<WorkoutSession> sessions = new ArrayList<>();
        String sql = """
        SELECT 
        ws.*,
        u1.first_name AS user1_first_name, u1.last_name AS user1_last_name,
        u2.first_name AS user2_first_name, u2.last_name AS user2_last_name
         FROM workout_sessions ws
         JOIN users u1 ON ws.user1_id = u1.id
         JOIN users u2 ON ws.user2_id = u2.id 
         WHERE (ws.user1_id = ? OR ws.user2_id = ?) AND ws.status = ?
         ORDER BY ws.scheduled_time ASC""";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setString(3, status.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WorkoutSession session = new WorkoutSession(
                        rs.getInt("session_id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("scheduled_time"),
                        WorkoutStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at")
                );
                session.setUser1FirstName(rs.getString("user1_first_name"));
                session.setUser1LastName(rs.getString("user1_last_name"));
                session.setUser2FirstName(rs.getString("user2_first_name"));
                session.setUser2LastName(rs.getString("user2_last_name"));
                sessions.add(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
}
