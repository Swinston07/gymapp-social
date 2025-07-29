package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.WorkoutSession;
import com.sterling.Models.WorkoutStatus;

public interface WorkoutSessionDAOInterface {
    WorkoutSession createSession(WorkoutSession session);
    WorkoutSession getSessionById(int sessionId);
    List<WorkoutSession> getSessionsByUserId(int userId);
    boolean updateSessionStatus(int sessionId, WorkoutStatus status);
    boolean deleteSession(int sessionId);
}
