package com.sterling.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.NotificationDAOInterface;
import com.sterling.Models.Notification;

public class NotificationDAO implements NotificationDAOInterface {

    @Override
    public long create(Notification n) {
        String sql = "INSERT INTO notifications (user_id, type, title, body, data_json) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getType());
            ps.setString(3, n.getTitle());
            ps.setString(4, n.getBody());
            ps.setString(5, n.getDataJson());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0L;
    }

    @Override
    public List<Notification> getForUser(int userId, int limit, int offset) {
        String sql = "SELECT * FROM notifications WHERE user_id=? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Notification> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, Math.max(1, limit));
            ps.setInt(3, Math.max(0, offset));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Notification(
                        rs.getLong("id"),
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getString("title"),
                        rs.getString("body"),
                        rs.getString("data_json"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("read_at")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void markRead(int userId, long notificationId) {
        String sql = "UPDATE notifications SET read_at = NOW() WHERE id = ? AND user_id = ? AND read_at IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, notificationId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void markAllRead(int userId) {
        String sql = "UPDATE notifications SET read_at = NOW() WHERE user_id = ? AND read_at IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
