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

import com.sterling.Interfaces.AssignedExerciseDAOInterface;
import com.sterling.Models.AssignedExercise;

public class AssignedExerciseServiceTest {
    AssignedExerciseDAOInterface assignedExerciseDAO;
    AssignedExerciseService assignedExerciseService;

    @BeforeEach
    public void setUp(){
        assignedExerciseDAO = mock(AssignedExerciseDAOInterface.class);
        assignedExerciseService = new AssignedExerciseService(assignedExerciseDAO);
    }
    @Test
    void testAddAssignedExercise() {
        AssignedExercise exercise = new AssignedExercise();

        assignedExerciseService.addAssignedExercise(exercise);
        verify(assignedExerciseDAO, times(1)).addAssignedExercise(exercise);

    }

    @Test
    void testDeleteAssignedExercise() {
        when(assignedExerciseDAO.deleteAssignedExercise(1)).thenReturn(true);

        boolean result = assignedExerciseService.deleteAssignedExercise(1);
        assertTrue(result);
        verify(assignedExerciseDAO, times(1)).deleteAssignedExercise(1);
    }

    @Test
    void testGetAssignedExerciseById() {
        AssignedExercise exercise = new AssignedExercise();
        exercise.setAssignedExerciseId(1);

        when(assignedExerciseDAO.getAssignedExerciseById(1)).thenReturn(exercise);

        AssignedExercise result = assignedExerciseService.getAssignedExerciseById(1);

        assertNotNull(result);
        assertEquals(1, result.getAssignedExerciseId());
        verify(assignedExerciseDAO, times(1)).getAssignedExerciseById(1);
    }

    @Test
    void testGetAssignedExercisesByAssignmentId() {
        List<AssignedExercise> exercises = List.of(new AssignedExercise(), new AssignedExercise());

        when(assignedExerciseDAO.getAssignedExercisesByAssignmentId(1)).thenReturn(exercises);

        List<AssignedExercise> result = assignedExerciseService.getAssignedExercisesByAssignmentId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(assignedExerciseDAO, times(1)).getAssignedExercisesByAssignmentId(1);
    }

    @Test
    void testUpdateAssignedExercise() {
        AssignedExercise exercise = new AssignedExercise();

        exercise.setAssignedExerciseId(1);
        exercise.setExerciseName("Bench Press");
        exercise.setSets(5);
        exercise.setReps(12);

        when(assignedExerciseDAO.updateAssignedExercise(exercise)).thenReturn(true);

        boolean result = assignedExerciseService.updateAssignedExercise(exercise);

        assertTrue(result);
        verify(assignedExerciseDAO, times(1)).updateAssignedExercise(exercise);
    }
}
