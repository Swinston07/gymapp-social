package com.sterling.Services;

import java.util.List;
import java.util.Map;

import com.sterling.Interfaces.MessageDAOInterface;
import com.sterling.Models.Message;

public class MessageService {
    private final MessageDAOInterface messageDao;
    private final NotificationService notificationService; // may be null if not wired yet

    // Preferred: inject NotificationService
    public MessageService(MessageDAOInterface messageDao, NotificationService notificationService) {
        this.messageDao = messageDao;
        this.notificationService = notificationService;
    }

    // Backwards-compatible: if you haven't wired NotificationService yet
    public MessageService(MessageDAOInterface messageDao) {
        this(messageDao, null);
    }

    public void sendMessage(Message message) {
        // 1) persist
        messageDao.sendMessage(message);

        // 2) push notify receiver (best-effort)
        if (notificationService != null) {
            try {
                notificationService.notifyUser(
                    message.getReceiverId(),
                    "NEW_MESSAGE",
                    "New message",
                    "You have a new message.",
                    Map.of("senderId", message.getSenderId())
                );
            } catch (Exception e) {
                // Don't fail the send if push fails
                e.printStackTrace();
            }
        }
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
        if (message != null && requesterId == message.getSenderId()) {
            return messageDao.deleteMessage(messageId, requesterId);
        }
        return false;
    }
}
