package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sterling.Interfaces.ExerciseDAOInterface;
import com.sterling.Models.Exercise;

public class ExerciseServiceTest {
ExerciseDAOInterface exerciseDAO;
ExerciseService exerciseService;

@BeforeEach
public void setup(){
    exerciseDAO = mock(ExerciseDAOInterface.class);
    exerciseService = new ExerciseService(exerciseDAO);
}

    @Test
    void testAddExercise() {
        Exercise exercise = new Exercise();

        exercise.setId(1);
        exercise.setName("Bench Press");

        exerciseService.addExercise(exercise);

        verify(exerciseDAO,times(1)).addExercise(exercise);

    }

    @Test
    void testDeleteExercise() {
        Exercise exercise = new Exercise();

        exercise.setId(1);
        exercise.setName("Bench Press");

        when(exerciseDAO.deleteExercise(1)).thenReturn(true);

        boolean result = exerciseService.deleteExercise(1);

        assertTrue(result);

        verify(exerciseDAO,times(1)).deleteExercise(exercise.getId());
    }

    @Test
    void testGetAllExercises() {
        Exercise exercise1 = new Exercise();

        exercise1.setId(1);
        exercise1.setName("Bench Press");

        Exercise exercise2 = new Exercise();

        exercise2.setId(2);
        exercise2.setName("Squat");

        List<Exercise> mockExerciseList = List.of(exercise1, exercise2);

        when(exerciseDAO.getAllExercises()).thenReturn(mockExerciseList);

        List<Exercise> exercises = exerciseService.getAllExercises();

        assertNotNull(exercises);
        assertEquals(2, exercises.size());
        assertEquals(1, exercise1.getId());
        assertEquals(2, exercise2.getId());
        assertEquals("Bench Press", exercises.get(0).getName());
        assertEquals("Squat", exercises.get(1).getName());
        
        verify(exerciseDAO, times(1)).getAllExercises();
    }

    @Test
    void testGetExerciseById() {
        Exercise exercise = new Exercise();

        exercise.setId(1);
        exercise.setName("Bench Press");

        when(exerciseDAO.getExerciseById(1)).thenReturn(exercise);

        Exercise found = exerciseService.getExerciseById(1);

        assertEquals(exercise.getId(), found.getId());
        assertEquals(exercise.getName(), found.getName());
        
        verify(exerciseDAO, times(1)).getExerciseById(exercise.getId());
    }

    @Test
    void testGetExercisesByUserId() {
        Exercise exercise1 = new Exercise();
        Exercise exercise2 = new Exercise();

        exercise1.setId(1);
        exercise1.setName("Bench Press");
        exercise1.setUserId(1);

        exercise2.setId(2);
        exercise2.setName("Squat");
        exercise2.setUserId(1);

        List<Exercise> mockExerciseList = List.of(exercise1, exercise2);

        when(exerciseDAO.getExercisesByUserId(1)).thenReturn(mockExerciseList);

        List<Exercise> excercises = exerciseService.getExercisesByUserId(1);

        assertNotNull(excercises);
        assertEquals(2, excercises.size());
        assertEquals("Bench Press", excercises.get(0).getName());
        assertEquals("Squat", excercises.get(1).getName());

        verify(exerciseDAO,times(1)).getExercisesByUserId(1);
    }

    @Test
    void testUpdateExercise(){
        Exercise exercise = new Exercise();
        exercise.setId(1);
        exercise.setName("Squat");
        exercise.setReps(10);
        exercise.setSets(4);
        exercise.setWeight(225.5f);
        exercise.setUserId(2);

        when(exerciseDAO.updateExercise(exercise)).thenReturn(true);

        boolean updated = exerciseService.updateExercise(exercise);

        assertTrue(updated);

        verify(exerciseDAO,times(1)).updateExercise(exercise);
    }
}
