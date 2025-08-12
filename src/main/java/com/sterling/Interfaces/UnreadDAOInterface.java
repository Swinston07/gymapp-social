package com.sterling.Interfaces;

import java.util.List;
import java.util.Map;

public interface UnreadDAOInterface {
    Map<String, Integer> getSummary(int userId);     // buddies, invites, sessions, reviews, messages
    void markSectionSeen(int userId, String section);
    void markMessagesRead(int userId, int otherUserId);
    List<Map<String, Integer>> getUnreadByPartner(int userId);
}
