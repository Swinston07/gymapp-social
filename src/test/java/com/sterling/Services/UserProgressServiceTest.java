package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sterling.Interfaces.UserProgressDAOInterface;
import com.sterling.Models.UserProgress;

public class UserProgressServiceTest {

    private UserProgressDAOInterface userProgressDAO;
    private UserProgressService userProgressService;

    @BeforeEach
    public void setup() {
        userProgressDAO = mock(UserProgressDAOInterface.class);
        userProgressService = new UserProgressService(userProgressDAO);
    }

    @Test
    void testAddUserProgress() {
        UserProgress progress = new UserProgress();

        progress.setUserId(1);
        progress.setWeight(100.0f);
        progress.setBodyFatPercentage(15.0f);

        userProgressService.addUserProgress(progress);

        verify(userProgressDAO, times(1)).addUserProgress(progress);
    }

    @Test
    void testDeleteProgressByUserId() {
        when(userProgressDAO.deleteProgressByUserId(1)).thenReturn(true);

        boolean deleted = userProgressService.deleteProgressByUserId(1);
        
        assertTrue(deleted);
        verify(userProgressDAO, times(1)).deleteProgressByUserId(1);
    }

    @Test
    void testGetProgressByUserId() {
        UserProgress p1 = new UserProgress();
        p1.setWeight(100);
        p1.setBodyFatPercentage(15);

        UserProgress p2 = new UserProgress();
        p2.setWeight(123);
        p2.setBodyFatPercentage(12);

        when(userProgressDAO.getProgressByUserId(1)).thenReturn(List.of(p1, p2));

        List<UserProgress> result = userProgressService.getProgressByUserId(1);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getWeight());
        assertEquals(15, result.get(0).getBodyFatPercentage());
        assertEquals(123, result.get(1).getWeight());
        assertEquals(12, result.get(1).getBodyFatPercentage());

        verify(userProgressDAO, times(1)).getProgressByUserId(1);
    }
}
