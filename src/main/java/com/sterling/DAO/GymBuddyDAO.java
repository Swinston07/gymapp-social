package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.GymBuddyDAOInterface;
//import com.sterling.Models.GymBuddy;
import com.sterling.Models.User;

public class GymBuddyDAO implements GymBuddyDAOInterface {
    @Override
    public void addGymBuddy(int userId, int buddyId){
        if (!exists(userId, buddyId)) {
            String sql = "INSERT INTO gym_buddies (user_id, buddy_id, created_at) VALUES (?, ?, NOW())";

            try(Connection conn = DBConnection.getConnection()){
                PreparedStatement ps = conn.prepareStatement(sql);
                
                ps.setInt(1, userId);
                ps.setInt(2, buddyId);

                ps.executeUpdate();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean exists(int userId, int buddyId){
        String sql = "SELECT 1 FROM gym_buddies WHERE user_id = ? AND buddy_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, buddyId);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getBuddiesByUserId(int userId){
        String sql = "SELECT u.id, u.username, u.first_name, u.last_name FROM gym_buddies gb JOIN users u ON gb.buddy_id = u.id WHERE gb.user_id = ?";
        List<User> buddies = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs =ps.executeQuery();

            while(rs.next()){
                buddies.add(
                    new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buddies;
    }
}
