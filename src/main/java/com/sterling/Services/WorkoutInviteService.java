package com.sterling.Services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.sterling.Interfaces.WorkoutInviteDAOInterface;
import com.sterling.Models.WorkoutInvite;

public class WorkoutInviteService {
    private WorkoutInviteDAOInterface workoutInviteDao;
    private GymBuddyService gymBuddyService;

    public WorkoutInviteService(WorkoutInviteDAOInterface workoutInviteDao, GymBuddyService gymBuddyService){
        this.workoutInviteDao = workoutInviteDao;
        this.gymBuddyService = gymBuddyService;
    }

    public String sendInvite(WorkoutInvite invite) {
        int senderId = invite.getSenderId();
        int recipientId = invite.getRecipientId();
        Timestamp now = Timestamp.from(Instant.now());

        //Check for reverse pending invite
        WorkoutInvite reverseInvite = workoutInviteDao.findPendingInvite(recipientId, senderId);

        if(reverseInvite != null){
            //Accept reverse invite
            reverseInvite.setStatus("accepted");
            reverseInvite.setRespondedAt(now);
            workoutInviteDao.updateInviteStatus(reverseInvite);

            //Save new invite as accepted
            invite.setStatus("accepted");
            invite.setSentAt(now);
            invite.setRespondedAt(now);
            workoutInviteDao.saveInvite(invite);

            //Add gym both users as gym buddies
            gymBuddyService.addGymBuddy(senderId, recipientId);
            gymBuddyService.addGymBuddy(recipientId, senderId);

            return "Invite Accepted!";
        } else {
            invite.setStatus("pending");
            invite.setSentAt(now);
            invite.setRespondedAt(null);
            workoutInviteDao.saveInvite(invite);

            return "Invite sent successfully!";
        }
    }

    public List<WorkoutInvite> getInviteForUser(int userId){
        return workoutInviteDao.getInvitesForUser(userId);
    }

    public boolean updateInviteStatus(int inviteId, String status, Timestamp respondedAt){
        return workoutInviteDao.updateInviteStatus(inviteId, status, respondedAt);
    }
}
