package com.sterling.Interfaces;

import java.sql.Timestamp;
import java.util.List;

import com.sterling.Models.WorkoutInvite;

public interface WorkoutInviteDAOInterface {
    void saveInvite(WorkoutInvite invite);
    List<WorkoutInvite> getInvitesForUser(int userId);
    boolean updateInviteStatus(int inviteId, String status, Timestamp respondedAt);
    public WorkoutInvite findPendingInvite(int senderId, int recipientId);
    public boolean updateInviteStatus(WorkoutInvite invite);
    WorkoutInvite getInviteById(int inviteId);
}
