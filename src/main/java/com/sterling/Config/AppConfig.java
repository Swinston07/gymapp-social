package com.sterling.Config;

import com.sterling.Controllers.*;
import com.sterling.DAO.*;
import com.sterling.Services.*;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    public static Map<String, Object> initializeDependencies() {
        Map<String, Object> beans = new HashMap<>();

        // ===== DAOs =====
        UserDAO userDAO = new UserDAO();
        ExerciseDAO exerciseDAO = new ExerciseDAO();
        AssignedWorkoutDAO assignedWorkoutDAO = new AssignedWorkoutDAO();
        AssignedExerciseDAO assignedExerciseDAO = new AssignedExerciseDAO();
        BlogPostDAO blogPostDAO = new BlogPostDAO();
        UserProgressDAO userProgressDAO = new UserProgressDAO();
        WorkoutInviteDAO workoutInviteDAO = new WorkoutInviteDAO();
        GymBuddyDAO gymBuddyDAO = new GymBuddyDAO();
        MessageDAO messageDAO = new MessageDAO();
        PhotoDAO photoDAO = new PhotoDAO();
        ReviewDAO reviewDAO = new ReviewDAO();
        WorkoutSessionDAO workoutSessionDAO = new WorkoutSessionDAO();

        // ===== Services =====
        UserService userService = new UserService(userDAO);
        ExerciseService exerciseService = new ExerciseService(exerciseDAO);
        AssignedWorkoutService assignedWorkoutService = new AssignedWorkoutService(assignedWorkoutDAO);
        AssignedExerciseService assignedExerciseService = new AssignedExerciseService(assignedExerciseDAO);
        BlogPostService blogPostService = new BlogPostService(blogPostDAO);
        UserProgressService userProgressService = new UserProgressService(userProgressDAO);
        GymBuddyService gymBuddyService = new GymBuddyService(gymBuddyDAO);
        WorkoutInviteService workoutInviteService = new WorkoutInviteService(workoutInviteDAO, gymBuddyService);
        MessageService messageService = new MessageService(messageDAO);
        PhotoService photoService = new PhotoService(photoDAO);
        ReviewService reviewService = new ReviewService(reviewDAO);
        WorkoutSessionService workoutSessionService = new WorkoutSessionService(workoutSessionDAO);

        // ===== Controllers =====
        UserController userController = new UserController(userService);
        ExerciseController exerciseController = new ExerciseController(exerciseService);
        AssignedWorkoutController assignedWorkoutController = new AssignedWorkoutController(assignedWorkoutService, userService);
        AssignedExerciseController assignedExerciseController = new AssignedExerciseController(assignedExerciseService, userService, assignedWorkoutService);
        BlogPostController blogPostController = new BlogPostController(blogPostService);
        UserProgressController userProgressController = new UserProgressController(userProgressService);
        WorkoutInviteController workoutInviteController = new WorkoutInviteController(workoutInviteService, userService);
        GymBuddyController gymBuddyController = new GymBuddyController(gymBuddyService);
        MessageController messageController = new MessageController(messageService);
        PhotoController photoController = new PhotoController(photoService);
        ReviewController reviewController = new ReviewController(reviewService);
        WorkoutSessionController workoutSessionController = new WorkoutSessionController(workoutSessionService);

        // ===== Register beans =====
        beans.put("userController", userController);
        beans.put("exerciseController", exerciseController);
        beans.put("assignedWorkoutController", assignedWorkoutController);
        beans.put("assignedExerciseController", assignedExerciseController);
        beans.put("blogPostController", blogPostController);
        beans.put("userProgressController", userProgressController);
        beans.put("workoutInviteController", workoutInviteController);
        beans.put("gymBuddyController", gymBuddyController);
        beans.put("messageController", messageController);
        beans.put("photoController", photoController);
        beans.put("reviewController", reviewController);
        beans.put("workoutSessionController", workoutSessionController);

        // StripeController uses static methods, so no instantiation needed

        return beans;
    }
}
