package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.MessageDAOInterface;
import com.sterling.Models.Message;

public class MessageDAO implements MessageDAOInterface {
    @Override
    public void sendMessage(Message message){
        String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
            ps.setString(3, message.getContent());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    @Override
    public List<Message> getMessagesBetweenUsers(int userId1, int userId2){
        String sql = "SELECT * FROM messages WHERE " +
        "(sender_id = ? AND receiver_id = ?) OR "  +
        "(sender_id = ? AND receiver_id = ?) " +
        "ORDER BY sent_at ASC";

        List<Message> messages = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);

            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                messages.add(
                    new Message(
                        rs.getInt("message_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("sent_at")
                    )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<Message> getMessagesForUser(int userId){
        String sql = "SELECT * FROM messages WHERE sender_id = ? OR receiver_id = ?";
        List<Message> messages = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                messages.add(
                    new Message(
                        rs.getInt("message_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("content"),
                        rs.getTimestamp("sent_at")
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM messages WHERE message_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("sender_id"),
                    rs.getInt("receiver_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_at")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteMessage(int messageId, int requesterId){
        String sql = "DELETE FROM messages WHERE message_id = ? AND sender_id = ?";

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, messageId);
            ps.setInt(2, requesterId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
