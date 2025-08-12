package com.sterling.Services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.sterling.Interfaces.WorkoutInviteDAOInterface;
import com.sterling.Models.WorkoutInvite;

public class WorkoutInviteService {
    private final WorkoutInviteDAOInterface workoutInviteDao;
    private final GymBuddyService gymBuddyService;
    private final NotificationService notificationService; // may be null if not wired yet

    // Preferred constructor
    public WorkoutInviteService(WorkoutInviteDAOInterface workoutInviteDao,
                                GymBuddyService gymBuddyService,
                                NotificationService notificationService) {
        this.workoutInviteDao = workoutInviteDao;
        this.gymBuddyService = gymBuddyService;
        this.notificationService = notificationService;
    }

    // Backwards-compatible constructor (no notifications)
    public WorkoutInviteService(WorkoutInviteDAOInterface workoutInviteDao,
                                GymBuddyService gymBuddyService) {
        this(workoutInviteDao, gymBuddyService, null);
    }

    public String sendInvite(WorkoutInvite invite) {
        int senderId = invite.getSenderId();
        int recipientId = invite.getRecipientId();
        Timestamp now = Timestamp.from(Instant.now());

        // Check for reverse pending invite
        WorkoutInvite reverseInvite = workoutInviteDao.findPendingInvite(recipientId, senderId);

        if (reverseInvite != null) {
            // Accept reverse invite
            reverseInvite.setStatus("accepted");
            reverseInvite.setRespondedAt(now);
            workoutInviteDao.updateInviteStatus(reverseInvite);

            // Save new invite as accepted (for audit/history)
            invite.setStatus("accepted");
            invite.setSentAt(now);
            invite.setRespondedAt(now);
            workoutInviteDao.saveInvite(invite);

            // Make gym buddies both ways
            gymBuddyService.addGymBuddy(senderId, recipientId);
            gymBuddyService.addGymBuddy(recipientId, senderId);

            // Notify both
            notifySafe(senderId,   "INVITE_ACCEPTED", "It’s a match!", "You’re now gym buddies. Say hi!", Map.of("buddyId", recipientId));
            notifySafe(recipientId,"INVITE_ACCEPTED", "It’s a match!", "You’re now gym buddies. Say hi!", Map.of("buddyId", senderId));

            return "Invite Accepted!";
        } else {
            // Normal pending invite
            invite.setStatus("pending");
            invite.setSentAt(now);
            invite.setRespondedAt(null);
            workoutInviteDao.saveInvite(invite);

            // Notify recipient
            notifySafe(recipientId, "NEW_INVITE", "New workout invite", "You’ve received a workout invite.", Map.of("senderId", senderId));
            return "Invite sent successfully!";
        }
    }

    public List<WorkoutInvite> getInvitesForUser(int userId){
        return workoutInviteDao.getInvitesForUser(userId);
    }

    public boolean updateInviteStatus(int inviteId, String status, Timestamp respondedAt){
        // Update first
        boolean ok = workoutInviteDao.updateInviteStatus(inviteId, status, respondedAt);
        if (!ok) return false;

        // Load invite context to notify + create buddies when accepted
        WorkoutInvite invite = workoutInviteDao.getInviteById(inviteId);
        if (invite == null) return true; // updated, but no context to notify

        String s = status == null ? "" : status.toLowerCase();
        if ("accepted".equals(s)) {
            // Create gym buddies
            gymBuddyService.addGymBuddy(invite.getSenderId(), invite.getRecipientId());
            gymBuddyService.addGymBuddy(invite.getRecipientId(), invite.getSenderId());

            // Notify both
            notifySafe(invite.getSenderId(),    "INVITE_ACCEPTED", "It’s a match!", "You’re now gym buddies. Say hi!", Map.of("buddyId", invite.getRecipientId()));
            notifySafe(invite.getRecipientId(), "INVITE_ACCEPTED", "It’s a match!", "You’re now gym buddies. Say hi!", Map.of("buddyId", invite.getSenderId()));
        } else if ("declined".equals(s)) {
            // Optional: inform sender
            notifySafe(invite.getSenderId(), "INVITE_DECLINED", "Invite declined", "Your workout invite was declined.", Map.of("recipientId", invite.getRecipientId()));
        }

        return true;
    }

    private void notifySafe(int userId, String type, String title, String body, Map<String, Object> data) {
        if (notificationService == null) return;
        try {
            notificationService.notifyUser(userId, type, title, body, data);
        } catch (Exception e) {
            // Never break core logic on push errors
            e.printStackTrace();
        }
    }
}
