package com.sterling.Interfaces;

import java.util.List;
import com.sterling.Models.Notification;

public interface NotificationDAOInterface {
    long create(Notification n);                 // returns id
    List<Notification> getForUser(int userId, int limit, int offset);
    void markRead(int userId, long notificationId);
    void markAllRead(int userId);
}
