package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sterling.Interfaces.WorkoutSessionDAOInterface;
import com.sterling.Models.WorkoutSession;
import com.sterling.Models.WorkoutStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WorkoutSessionServiceTest {

    private WorkoutSessionDAOInterface sessionDao;
    private WorkoutSessionService sessionService;

    @BeforeEach
    public void setUp() {
        sessionDao = mock(WorkoutSessionDAOInterface.class);
        sessionService = new WorkoutSessionService(sessionDao);
    }

    @Test
    void testCreateSession() {
        WorkoutSession session = new WorkoutSession();
        session.setSessionId(1);
        session.setUser1Id(1);
        session.setUser2Id(2);
        session.setStatus(WorkoutStatus.PENDING);

        when(sessionDao.createSession(session)).thenReturn(session);

        WorkoutSession result = sessionService.createSession(session);

        assertNotNull(result);
        assertEquals(WorkoutStatus.PENDING, result.getStatus());
        verify(sessionDao, times(1)).createSession(session);
    }

    @Test
    void testGetSessionById() {
        WorkoutSession session = new WorkoutSession();
        session.setSessionId(1);

        when(sessionDao.getSessionById(1)).thenReturn(session);

        WorkoutSession result = sessionService.getSessionById(1);

        assertNotNull(result);
        assertEquals(1, result.getSessionId());
        verify(sessionDao, times(1)).getSessionById(1);
    }

    @Test
    void testGetSessionsByUserId() {
        WorkoutSession s1 = new WorkoutSession();
        WorkoutSession s2 = new WorkoutSession();

        when(sessionDao.getSessionsByUserId(1)).thenReturn(List.of(s1, s2));

        List<WorkoutSession> result = sessionService.getSessionsByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sessionDao, times(1)).getSessionsByUserId(1);
    }

    @Test
    void testGetSessionsByUserIdAndStatus() {
        WorkoutSession s1 = new WorkoutSession();
        s1.setStatus(WorkoutStatus.PENDING);

        when(sessionDao.getSessionsByUserIdAndStatus(1, WorkoutStatus.PENDING))
                .thenReturn(List.of(s1));

        List<WorkoutSession> result = sessionService.getSessionsByUserIdAndStatus(1, WorkoutStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(WorkoutStatus.PENDING, result.get(0).getStatus());
        verify(sessionDao, times(1)).getSessionsByUserIdAndStatus(1, WorkoutStatus.PENDING);
    }

    @Test
    void testUpdateSessionStatus() {
        when(sessionDao.updateSessionStatus(1, WorkoutStatus.COMPLETED)).thenReturn(true);

        boolean updated = sessionService.updateSessionStatus(1, WorkoutStatus.COMPLETED);

        assertTrue(updated);
        verify(sessionDao, times(1)).updateSessionStatus(1, WorkoutStatus.COMPLETED);
    }

    @Test
    void testDeleteSession() {
        when(sessionDao.deleteSession(1)).thenReturn(true);

        boolean deleted = sessionService.deleteSession(1);

        assertTrue(deleted);
        verify(sessionDao, times(1)).deleteSession(1);
    }
}