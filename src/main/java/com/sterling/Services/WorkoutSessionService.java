package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.WorkoutSessionDAOInterface;
import com.sterling.Models.WorkoutSession;
import com.sterling.Models.WorkoutStatus;

public class WorkoutSessionService {
    public final WorkoutSessionDAOInterface workoutSessionDao;

    public WorkoutSessionService(WorkoutSessionDAOInterface workoutSessionDao) {
        this.workoutSessionDao = workoutSessionDao;
    }

    public WorkoutSession createSession(WorkoutSession session) {
        return workoutSessionDao.createSession(session);
    }

    public WorkoutSession getSessionById(int sessionId) {
        return workoutSessionDao.getSessionById(sessionId);
    }

    public List<WorkoutSession> getSessionsByUserId(int userId) {
        return workoutSessionDao.getSessionsByUserId(userId);
    }

    public boolean updateSessionStatus(int sessionId, WorkoutStatus status) {
        return workoutSessionDao.updateSessionStatus(sessionId, status);
    }

    public boolean deleteSession(int sessionId) {
        return workoutSessionDao.deleteSession(sessionId);
    }
}
