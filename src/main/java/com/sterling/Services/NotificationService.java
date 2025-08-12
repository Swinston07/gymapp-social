package com.sterling.Services;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sterling.Interfaces.DeviceTokenDAOInterface;
import com.sterling.Interfaces.NotificationDAOInterface;
import com.sterling.Models.Notification;
import com.sterling.Push.PushGateway;

public class NotificationService {
    private final NotificationDAOInterface notificationDao;
    private final DeviceTokenDAOInterface deviceTokenDao;
    private final PushGateway pushGateway;
    private final ObjectMapper mapper = new ObjectMapper();

    public NotificationService(NotificationDAOInterface notificationDao,
                               DeviceTokenDAOInterface deviceTokenDao,
                               PushGateway pushGateway) {
        this.notificationDao = notificationDao;
        this.deviceTokenDao = deviceTokenDao;
        this.pushGateway = pushGateway;
    }

    public long notifyUser(int userId, String type, String title, String body, Map<String,Object> data){
        String json = null;
        try { if (data != null) json = mapper.writeValueAsString(data); }
        catch (JsonProcessingException ignored) {}
        Notification n = new Notification(0, userId, type, title, body, json, null, null);
        long id = notificationDao.create(n);

        List<String> tokens = deviceTokenDao.getActiveTokensForUser(userId);
        if (!tokens.isEmpty()) pushGateway.send(tokens, title, body, data);
        return id;
    }

    public List<Notification> getForUser(int userId, int limit, int offset){
        return notificationDao.getForUser(userId, limit, offset);
    }

    public void markRead(int userId, long notificationId){
        notificationDao.markRead(userId, notificationId);
    }

    public void markAllRead(int userId){
        notificationDao.markAllRead(userId);
    }
}
