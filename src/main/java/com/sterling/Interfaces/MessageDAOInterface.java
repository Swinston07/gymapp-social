package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.Message;

public interface MessageDAOInterface {
    void sendMessage(Message message);
    List<Message> getMessagesBetweenUsers(int userId1, int userId2);
    List<Message> getMessagesForUser(int userId);
    Message getMessageById(int messageId);
    boolean deleteMessage(int messageId, int requesterId);
}
