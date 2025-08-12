// src/main/java/com/sterling/Services/UnreadService.java
package com.sterling.Services;

import com.sterling.Interfaces.UnreadDAOInterface;

import java.util.List;
import java.util.Map;

public class UnreadService {
    private final UnreadDAOInterface unreadDao;

    public UnreadService(UnreadDAOInterface unreadDao) {
        this.unreadDao = unreadDao;
    }

    public Map<String, Integer> getSummary(int userId) {
        return unreadDao.getSummary(userId);
    }

    public void markSectionSeen(int userId, String section) {
        unreadDao.markSectionSeen(userId, section);
    }

    public void markMessagesRead(int userId, int otherUserId) {
        unreadDao.markMessagesRead(userId, otherUserId);
    }

    // in UnreadService.java
    public List<Map<String, Integer>> getUnreadByPartner(int userId) {
        return unreadDao.getUnreadByPartner(userId);
    }
}
