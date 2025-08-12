package com.sterling.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.DeviceTokenDAOInterface;

public class DeviceTokenDAO implements DeviceTokenDAOInterface {

    @Override
    public void upsertToken(int userId, String token, String platform) {
        // If token exists for another user or was revoked, re-attach to this user & reactivate
        String sql = """
            INSERT INTO device_tokens (user_id, token, platform)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
              user_id = VALUES(user_id),
              platform = VALUES(platform),
              revoked_at = NULL
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setString(3, platform.toLowerCase());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void revokeToken(int userId, String token) {
        String sql = "UPDATE device_tokens SET revoked_at = NOW() WHERE user_id = ? AND token = ? AND revoked_at IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<String> getActiveTokensForUser(int userId) {
        String sql = "SELECT token FROM device_tokens WHERE user_id = ? AND revoked_at IS NULL";
        List<String> tokens = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) tokens.add(rs.getString("token"));
        } catch (SQLException e) { e.printStackTrace(); }
        return tokens;
    }
}
