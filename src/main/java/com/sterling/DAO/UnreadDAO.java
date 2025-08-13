// src/main/java/com/sterling/DAO/UnreadDAO.java
package com.sterling.DAO;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.UnreadDAOInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnreadDAO implements UnreadDAOInterface {

    @Override
    public Map<String, Integer> getSummary(int userId) {
        Map<String, Integer> m = new HashMap<>();
        m.put("buddies",  countBuddies(userId));
        m.put("invites",  countInvites(userId));   // workout_invites + PENDING sessions to user2
        m.put("sessions", countSessions(userId));  // only SCHEDULED sessions (created/updated)
        m.put("reviews",  countReviews(userId));
        m.put("messages", countMessages(userId));  // new inbound messages since 'messages' seen
        return m;
    }

    @Override
    public void markSectionSeen(int userId, String section) {
        final String sql = """
            INSERT INTO user_section_seen (user_id, section, last_seen_at)
            VALUES (?, ?, NOW())
            ON DUPLICATE KEY UPDATE last_seen_at = NOW()
            """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, section.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // IMPORTANT: flip messages.read_at for this thread so per-partner dots clear
    @Override
    public void markMessagesRead(int userId, int otherUserId) {
        final String sql = """
            UPDATE messages
            SET read_at = NOW()
            WHERE receiver_id = ? AND sender_id = ? AND read_at IS NULL
            """;
        try (Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);       // the viewer
            ps.setInt(2, otherUserId);  // the partner
            ps.executeUpdate();         // row count ignored (interface is void)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* ----------------- private counters ----------------- */

    private int countBuddies(int userId) {
        final String sql = """
            SELECT COUNT(*)
            FROM gym_buddies gb
            WHERE gb.user_id = ?
              AND gb.created_at >
                  COALESCE(
                      (SELECT last_seen_at FROM user_section_seen
                        WHERE user_id = ? AND section = 'buddies'),
                      '1970-01-01 00:00:01'
                  )
            """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // PENDING invites: workout_invites + PENDING sessions addressed to user2
    private int countInvites(int userId) {
        final String sql = """
            SELECT
              (
                SELECT COUNT(*)
                FROM workout_invites wi
                WHERE wi.recipient_id = ?
                  AND wi.status = 'pending'
                  AND wi.sent_at >
                      COALESCE(
                          (SELECT last_seen_at FROM user_section_seen
                           WHERE user_id = ? AND section = 'invites'),
                          '1970-01-01 00:00:01'
                      )
              )
              +
              (
                SELECT COUNT(*)
                FROM workout_sessions ws
                WHERE ws.user2_id = ?
                  AND ws.status = 'PENDING'
                  AND ws.created_at >
                      COALESCE(
                          (SELECT last_seen_at FROM user_section_seen
                           WHERE user_id = ? AND section = 'invites'),
                          '1970-01-01 00:00:01'
                      )
              ) AS cnt
            """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Only SCHEDULED sessions; consider both created_at and updated_at
    private int countSessions(int userId) {
        final String sql = """
            SELECT COUNT(*)
            FROM workout_sessions ws
            WHERE (ws.user1_id = ? OR ws.user2_id = ?)
              AND ws.status = 'SCHEDULED'
              AND (
                    ws.created_at >
                      COALESCE(
                        (SELECT last_seen_at FROM user_section_seen
                         WHERE user_id = ? AND section = 'sessions'),
                        '1970-01-01 00:00:01'
                      )
                 OR (ws.updated_at IS NOT NULL AND ws.updated_at >
                      COALESCE(
                        (SELECT last_seen_at FROM user_section_seen
                         WHERE user_id = ? AND section = 'sessions'),
                        '1970-01-01 00:00:01'
                      ))
              )
            """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int countReviews(int userId) {
        final String sql = """
            SELECT COUNT(*)
            FROM reviews r
            WHERE r.reviewed_id = ?
              AND r.created_at >
                  COALESCE(
                      (SELECT last_seen_at FROM user_section_seen
                       WHERE user_id = ? AND section = 'reviews'),
                      '1970-01-01 00:00:01'
                  )
            """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // If you don't have a reviews table yet, you can temporarily return 0
            e.printStackTrace();
        }
        return 0;
    }

    // Global "messages" dot: new inbound since last time the section was seen
    private int countMessages(int userId) {
        final String sql = """
            SELECT COUNT(*)
            FROM messages m
            WHERE m.receiver_id = ?
              AND m.sent_at >
                  COALESCE(
                      (SELECT last_seen_at FROM user_section_seen
                       WHERE user_id = ? AND section = 'messages'),
                      '1970-01-01 00:00:01'
                  )
            """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Map<String, Integer>> getUnreadByPartner(int userId) {
        final String sql = """
            SELECT sender_id AS partner_id, COUNT(*) AS unread
            FROM messages
            WHERE receiver_id = ? AND read_at IS NULL
            GROUP BY sender_id
        """;
        List<Map<String, Integer>> rows = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Integer> row = new HashMap<>();
                    row.put("partner_id", rs.getInt("partner_id"));
                    row.put("unread", rs.getInt("unread"));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
