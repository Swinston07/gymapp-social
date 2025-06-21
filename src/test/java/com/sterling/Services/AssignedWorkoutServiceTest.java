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

import com.sterling.Interfaces.AssignedWorkoutDAOInterface;
import com.sterling.Models.AssignedWorkout;

public class AssignedWorkoutServiceTest {
    AssignedWorkoutDAOInterface assignedWorkoutDAO;
    AssignedWorkoutService assignedWorkoutService;

    @BeforeEach
    public void setUp(){
        assignedWorkoutDAO = mock(AssignedWorkoutDAOInterface.class);
        assignedWorkoutService = new AssignedWorkoutService(assignedWorkoutDAO);
    }

    @Test
    void testAssignWorkout() {
        AssignedWorkout workout = new AssignedWorkout();

        assignedWorkoutService.assignWorkout(workout);
        verify(assignedWorkoutDAO, times(1)).assignWorkout(workout);
    }

    @Test
    void testDeleteAssignment() {
        when(assignedWorkoutDAO.deleteAssignment(1)).thenReturn(true);
        boolean result = assignedWorkoutService.deleteAssignment(1);
        assertTrue(result);
        verify(assignedWorkoutDAO, times(1)).deleteAssignment(1);
    }

    @Test
    void testGetWorkoutById() {
        AssignedWorkout workout = new AssignedWorkout();
        workout.setAssignmentId(1);

        when(assignedWorkoutDAO.getById(1)).thenReturn(workout);

        AssignedWorkout result = assignedWorkoutService.getWorkoutById(1);

        assertNotNull(result);
        assertEquals(1, result.getAssignmentId());
        verify(assignedWorkoutDAO, times(1)).getById(1);
    }

    @Test
    void testGetWorkoutsByClientId() {
        List<AssignedWorkout> workouts = List.of(new AssignedWorkout(), new AssignedWorkout());

        when(assignedWorkoutDAO.getWorkoutsByClientId(2)).thenReturn(workouts);

        List<AssignedWorkout> result = assignedWorkoutService.getWorkoutsByClientId(2);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(assignedWorkoutDAO, times(1)).getWorkoutsByClientId(2);

    }

    @Test
    void testGetWorkoutsByTrainerId() {
        List<AssignedWorkout> workouts = List.of(new AssignedWorkout(), new AssignedWorkout());

        when(assignedWorkoutDAO.getWorkoutsByTrainerId(1)).thenReturn(workouts);

        List<AssignedWorkout> result = assignedWorkoutService.getWorkoutsByTrainerId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(assignedWorkoutDAO, times(1)).getWorkoutsByTrainerId(1);
    }

    @Test
    void testMarkWorkoutCompleted() {
        when(assignedWorkoutDAO.markWorkoutCompleted(1)).thenReturn(true);

        boolean result = assignedWorkoutService.markWorkoutCompleted(1);

        assertTrue(result);
        verify(assignedWorkoutDAO, times(1)).markWorkoutCompleted(1);
    }
}
