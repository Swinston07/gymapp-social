package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.UserProgressDAOInterface;
import com.sterling.Models.UserProgress;

public class UserProgressDAO implements UserProgressDAOInterface {
    
    @Override
    public void addUserProgress(UserProgress progress) {
        String sql = "INSERT INTO user_progress(user_id, weight, body_fat_percentage) VALUES (?, ?, ?)";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, progress.getUserId());
            ps.setFloat(2, progress.getWeight());
            ps.setFloat(3, progress.getBodyFatPercentage());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<UserProgress> getProgressByUserId(int userId){
        String sql = "SELECT * FROM user_progress WHERE user_id = ? ORDER BY recorded_at ASC";
        List<UserProgress> progressList = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                progressList.add(
                    new UserProgress(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getFloat("weight"),
                        rs.getFloat("body_fat_percentage"),
                        rs.getTimestamp("recorded_at")
                    )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return progressList;
    }

    @Override
    public boolean deleteProgressByUserId(int userId){
        String sql = "DELETE FROM user_progress WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
