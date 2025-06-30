package com.sterling.Services;

import java.sql.Timestamp;
import java.util.List;

import com.sterling.Interfaces.WorkoutInviteDAOInterface;
import com.sterling.Models.WorkoutInvite;

public class WorkoutInviteService {
    private WorkoutInviteDAOInterface workoutInviteDao;

    public WorkoutInviteService(WorkoutInviteDAOInterface workoutInviteDao){
        this.workoutInviteDao = workoutInviteDao;
    }

    public void sendInvite(WorkoutInvite invite) {
        workoutInviteDao.sendInvite(invite);
    }

    public List<WorkoutInvite> getInviteForUser(int userId){
        return workoutInviteDao.getInvitesForUser(userId);
    }

    public boolean updateInviteStatus(int inviteId, String status, Timestamp respondedAt){
        return workoutInviteDao.updateInviteStatus(inviteId, status, respondedAt);
    }
}
