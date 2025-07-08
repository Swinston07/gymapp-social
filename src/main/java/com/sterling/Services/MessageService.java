package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.MessageDAOInterface;
import com.sterling.Models.Message;

public class MessageService {
    private MessageDAOInterface messageDao;

    public MessageService(MessageDAOInterface messageDao) {
        this.messageDao = messageDao;
    }

    public void sendMessage(Message message) {
        messageDao.sendMessage(message);
    }

    public List<Message> getMessagesBetweenUsers(int userId1, int userId2) {
        return messageDao.getMessagesBetweenUsers(userId1, userId2);
    }

    public List<Message> getMessagesForUser(int userId) {
        return messageDao.getMessagesForUser(userId);
    }

    public Message getMessageById(int messageId) {
        return messageDao.getMessageById(messageId);
    }

    public boolean deleteMessage(int messageId, int requesterId) {
        Message message = messageDao.getMessageById(messageId);

        if(message != null && requesterId == message.getSenderId()){
            return messageDao.deleteMessage(messageId, requesterId);
        }
        return false;
    }
}
