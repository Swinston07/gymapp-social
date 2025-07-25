package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.WorkoutInviteDAOInterface;
import com.sterling.Models.WorkoutInvite;

public class WorkoutInviteDAO implements WorkoutInviteDAOInterface {
    @Override
    public void saveInvite(WorkoutInvite invite){
        String sql = "INSERT INTO workout_invites (sender_id, recipient_id, status, message, sent_at, responded_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, invite.getSenderId());
            ps.setInt(2, invite.getRecipientId());
            ps.setString(3, invite.getStatus());
            ps.setString(4, invite.getMessage());
            ps.setTimestamp(5, invite.getSentAt());
            ps.setTimestamp(6, invite.getRespondedAt());

            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<WorkoutInvite> getInvitesForUser(int userId){
        String sql = "SELECT * FROM workout_invites WHERE recipient_id = ?";
        List<WorkoutInvite> invites = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                invites.add(
                    new WorkoutInvite(
                        rs.getInt("id"), 
                        rs.getInt("sender_id"),
                        rs.getInt("recipient_id"), 
                        rs.getString("status"), 
                        rs.getString("message"), 
                        rs.getTimestamp("sent_at"),
                        rs.getTimestamp("responded_at"))
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return invites;
    }

    @Override
    public boolean updateInviteStatus(int inviteId, String status, Timestamp respondedAt){
        String sql = "UPDATE workout_invites SET status = ?, responded_at = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, status);
            ps.setTimestamp(2, respondedAt);
            ps.setInt(3, inviteId);

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public WorkoutInvite findPendingInvite(int senderId, int recipientId){
        String sql = "SELECT * FROM workout_invites WHERE sender_id = ? AND recipient_id = ? AND status = 'pending'";

        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, senderId);
            ps.setInt(2, recipientId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new WorkoutInvite(
                    rs.getInt("id"),
                    rs.getInt("sender_id"),
                    rs.getInt("recipient_id"),
                    rs.getString("status"),
                    rs.getString("message"),
                    rs.getTimestamp("sent_at"),
                    rs.getTimestamp("responded_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateInviteStatus(WorkoutInvite invite) {
        String sql = "UPDATE workout_invites SET status = ?, responded_at = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, invite.getStatus());
            ps.setTimestamp(2, invite.getRespondedAt());
            ps.setInt(3, invite.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
