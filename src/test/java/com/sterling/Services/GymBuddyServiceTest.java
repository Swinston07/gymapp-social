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

import com.sterling.Interfaces.GymBuddyDAOInterface;
import com.sterling.Models.User;

public class GymBuddyServiceTest {

    private GymBuddyDAOInterface gymBuddyDAO;
    private GymBuddyService gymBuddyService;

    @BeforeEach
    public void setup() {
        gymBuddyDAO = mock(GymBuddyDAOInterface.class);
        gymBuddyService = new GymBuddyService(gymBuddyDAO);
    }
    @Test
    void testAddGymBuddy() {
        gymBuddyService.addGymBuddy(1, 2);

        verify(gymBuddyDAO, times(1)).addGymBuddy(1, 2);
    }

    @Test
    void testExists() {
        when(gymBuddyDAO.exists(1, 2)).thenReturn(true);
        
        boolean result = gymBuddyService.exists(1, 2);
        
        assertTrue(result);
        verify(gymBuddyDAO, times(1)).exists(1, 2);
    }

    @Test
    void testGetGymBuddiesByUserId() {
        User buddy1 = new User();
        buddy1.setId(2);
        buddy1.setUsername("buddy1");

        User buddy2 = new User();
        buddy2.setId(3);
        buddy2.setUsername("buddy2");

        List<User> mockBuddies = List.of(buddy1, buddy2);

        when(gymBuddyDAO.getBuddiesByUserId(1)).thenReturn(mockBuddies);

        List<User> result = gymBuddyService.getGymBuddiesByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("buddy1", result.get(0).getUsername());
        assertEquals("buddy2", result.get(1).getUsername());

        verify(gymBuddyDAO, times(1)).getBuddiesByUserId(1);
    }
}
