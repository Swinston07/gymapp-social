package com.sterling.Services;

import java.util.List;
import java.util.Map;

import com.sterling.Interfaces.GymBuddyDAOInterface;
import com.sterling.Models.User;

public class GymBuddyService {
    private final GymBuddyDAOInterface gymBuddyDao;
    private final NotificationService notificationService; // optional

    // Preferred: inject NotificationService
    public GymBuddyService(GymBuddyDAOInterface gymBuddyDao, NotificationService notificationService){
        this.gymBuddyDao = gymBuddyDao;
        this.notificationService = notificationService;
    }

    // Backwards-compatible: no notifications
    public GymBuddyService(GymBuddyDAOInterface gymBuddyDao){
        this(gymBuddyDao, null);
    }

    public void addGymBuddy(int userId, int buddyId){
        // Only add + notify if this direction doesn't already exist
        if (!gymBuddyDao.exists(userId, buddyId)) {
            gymBuddyDao.addGymBuddy(userId, buddyId);
            notifySafe(buddyId, userId); // tell buddy they have a new connection
        }
    }

    public boolean exists(int userId, int buddyId){
        return gymBuddyDao.exists(userId, buddyId);
    }

    public List<User> getGymBuddiesByUserId(int userId){
        return gymBuddyDao.getBuddiesByUserId(userId);
    }

    private void notifySafe(int targetUserId, int newBuddyId) {
        if (notificationService == null) return;
        try {
            notificationService.notifyUser(
                targetUserId,
                "NEW_BUDDY",
                "New gym buddy",
                "You’re now buddies with user #" + newBuddyId,
                Map.of("userId", newBuddyId)
            );
        } catch (Exception e) {
            // never break the core flow if push fails
            e.printStackTrace();
        }
    }
}
