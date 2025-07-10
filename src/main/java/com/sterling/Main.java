package com.sterling;

import com.sterling.Controllers.AssignedExerciseController;
import com.sterling.Controllers.AssignedWorkoutController;
import com.sterling.Controllers.BlogPostController;
import com.sterling.Controllers.ExerciseController;
import com.sterling.Controllers.GymBuddyController;
import com.sterling.Controllers.MessageController;
import com.sterling.Controllers.PhotoController;
import com.sterling.Controllers.UserController;
import com.sterling.Controllers.UserProgressController;
import com.sterling.Controllers.WorkoutInviteController;
import com.sterling.DAO.AssignedExerciseDAO;
import com.sterling.DAO.AssignedWorkoutDAO;
import com.sterling.DAO.BlogPostDAO;
import com.sterling.DAO.ExerciseDAO;
import com.sterling.DAO.GymBuddyDAO;
import com.sterling.DAO.MessageDAO;
import com.sterling.DAO.UserDAO;
import com.sterling.DAO.UserProgressDAO;
import com.sterling.DAO.WorkoutInviteDAO;
import com.sterling.Services.AssignedExerciseService;
import com.sterling.Services.AssignedWorkoutService;
import com.sterling.Services.BlogPostService;
import com.sterling.Services.ExerciseService;
import com.sterling.Services.GymBuddyService;
import com.sterling.Services.MessageService;
import com.sterling.Services.UserProgressService;
import com.sterling.Services.UserService;
import com.sterling.Services.WorkoutInviteService;
import com.sterling.Utils.JwtUtil;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        UserDAO userDAO = new UserDAO();
        ExerciseDAO exerciseDAO = new ExerciseDAO();
        AssignedWorkoutDAO assignedWorkoutDAO = new AssignedWorkoutDAO();
        AssignedExerciseDAO assignedExerciseDAO = new AssignedExerciseDAO();
        BlogPostDAO blogPostDAO = new BlogPostDAO();
        UserProgressDAO userProgressDAO = new UserProgressDAO();
        WorkoutInviteDAO workoutInviteDao = new WorkoutInviteDAO();
        GymBuddyDAO gymBuddyDAO = new GymBuddyDAO();
        MessageDAO messageDAO = new MessageDAO();

        UserService userService = new UserService(userDAO);
        ExerciseService exerciseService = new ExerciseService(exerciseDAO);
        AssignedWorkoutService assignedWorkoutService = new AssignedWorkoutService(assignedWorkoutDAO);
        AssignedExerciseService assignedExerciseService = new AssignedExerciseService(assignedExerciseDAO);
        BlogPostService blogPostService = new BlogPostService(blogPostDAO);
        UserProgressService userProgressService = new UserProgressService(userProgressDAO);
        GymBuddyService gymBuddyService = new GymBuddyService(gymBuddyDAO);
        WorkoutInviteService workoutInviteService = new WorkoutInviteService(workoutInviteDao, gymBuddyService);
        MessageService messageService = new MessageService(messageDAO);

        UserController userController = new UserController(userService);
        ExerciseController exerciseController = new ExerciseController(exerciseService);
        AssignedWorkoutController assignedWorkoutController = new AssignedWorkoutController(assignedWorkoutService, userService);
        AssignedExerciseController assignedExerciseController = new AssignedExerciseController(assignedExerciseService, userService, assignedWorkoutService);
        BlogPostController blogPostController = new BlogPostController(blogPostService);
        UserProgressController userProgressController = new UserProgressController(userProgressService);
        WorkoutInviteController workoutInviteController = new WorkoutInviteController(workoutInviteService);
        GymBuddyController gymBuddyController = new GymBuddyController(gymBuddyService);
        MessageController messageController = new MessageController(messageService);
        PhotoController photoController = new PhotoController();

        app.before(ctx->{
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });
        app.options("/*",ctx->{
            ctx.status(204);
        });

        app.before("/users/*", Main::protectRoute);
        app.before("/trainers/*", Main::protectRoute);
        app.before("/exercises/*", Main::protectRoute);
        app.before("/assigned-workouts/*", Main::protectRoute);
        app.before("/assigned-exercises/*", Main::protectRoute);
        app.before("/blog-posts/*", Main::protectRoute);
        app.before("/users/*/blog-posts", Main::protectRoute);
        app.before("/progress/*", Main::protectRoute);
        app.before("/users/*/workout-invites*", Main::protectRoute);
        app.before("/users/*/gym-buddies*", Main::protectRoute);
        app.before("/messages/*", Main::protectRoute);
        

        app.start(7000);

        //User Routes
        app.post("/users", userController::registerUser);
        app.post("/login", userController::loginUser);
        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.get("/trainers/{trainerId}/clients", userController::getClientsByTrainerId);
        app.get("/users/{id}/nearby-gyms", userController::getNearByGyms);
        app.get("/users/{id}/matches", userController::findUsersByHomeGym);
        app.put("/users/{id}", userController::updateUser);
        app.put("/users/{id}/role", userController::updateUserRole);
        app.put("/users/{id}/update-location", userController::updateHomeGym);
        app.put("/users/{id}/toggle-workout", userController::toggleWorkoutStatus);
        app.put("/trainers/{trainerId}/clients/{clientId}", userController::assignClientToTrainer);
        app.delete("/users/{id}", userController::deleteUser);

        //Exercise Routes
        app.post("/users/{id}/exercises", exerciseController::addExercise);
        app.get("/exercises", exerciseController::getAllExercises);
        app.get("/exercises/{id}", exerciseController::getExerciseById);
        app.get("/users/{id}/exercises", exerciseController::getExercisesByUserId);
        app.put("/exercises/{id}", exerciseController::updateExercise);
        app.delete("/exercises/{id}", exerciseController::deleteExercise);

        //Assigned workouts route
        app.post("/trainers/{trainerId}/clients/{clientId}/assigned-workouts", assignedWorkoutController::assignWorkout);
        app.get("/assigned-workouts/client/{clientId}", assignedWorkoutController::getWorkoutsByClientId);
        app.get("/assigned-workouts/trainer/{trainerId}", assignedWorkoutController::getWorkoutsByTrainerId);
        app.get("/assigned-workouts/client/{clientId}/date/{date}",assignedWorkoutController::getWorkoutsByClientIdAndDate);
        app.put("/assigned-workouts/{assignmentId}/complete", assignedWorkoutController::markWorkoutCompleted);
        app.delete("/assigned-workouts/{assignmentId}", assignedWorkoutController::deleteAssignment);

        //Assigned Exercises Routes
        app.post("/assigned-exercises/{assignmentId}", assignedExerciseController::addAssignedExercise);
        app.get("/assigned-exercises/{assignmentId}", assignedExerciseController::getAssignedExercisesByAssignmentId);
        app.put("/assigned-exercises/{assignedExerciseId}", assignedExerciseController::updateAssignedExercise);
        app.put("/assigned-exercises/{assignedExerciseId}/completed", assignedExerciseController::markExerciseCompleted);
        app.delete("/assigned-exercises/{assignedExerciseId}", assignedExerciseController::deleteAssignedExercise);

        //Blog Post Routes
        app.post("/users/{userId}/blog-posts", blogPostController::createPost);
        app.get("/blog-posts", blogPostController::getAllBlogPosts);
        app.get("/blog-posts/{postId}", blogPostController::getPostById);
        app.get("/users/{userId}/blog-posts", blogPostController::getPostsByUserId);
        app.put("/blog-posts/{postId}", blogPostController::updatePost);
        app.delete("/blog-posts/{postId}", blogPostController::deletePost);

        //Progress Routes
        app.post("/progress/{userId}", userProgressController::addUserProgress);
        app.get("/progress/{userId}", userProgressController::getProgressByUserId);
        app.delete("/progress/{userId}", userProgressController::deleteProgressByUserId);

        //Workoutinvite endpoints
        app.post("/users/{id}/workout-invites/{recipientId}", workoutInviteController::sendInvite);
        app.get("/users/{id}/workout-invites", workoutInviteController::getInvitesForUser);
        app.put("/users/{id}/workout-invites/{inviteId}", workoutInviteController::updateInviteStatus);

        //Gym Buddy Routes
        app.post("/users/{id}/gym-buddies/{buddyId}", gymBuddyController::addGymBuddy);
        app.get("/users/{id}/gym-buddies", gymBuddyController::getGymBuddiesByUserId);
        app.get("/users/{id}/gym-buddies/{buddyId}", gymBuddyController::exists);

        //Message Routes
        app.post("/messages/{id}/{receiverId}", messageController::sendMessage);
        app.get("/messages/{id}/{otherUserId}", messageController::getMessagesBetweenUsers);
        app.get("/messages/{id}", messageController::getMessagesForUser);
        app.delete("/messages/{messageId}", messageController::deleteMessage);

        //Photo Routes
        app.post("/upload-photo", photoController::uploadPhoto);
    }

    public static void protectRoute(io.javalin.http.Context ctx){
        String authHeader = ctx.header("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            ctx.status(401).result("Missing or invalid token");
            return;
        }

        String token = authHeader.replace("Bearer ", "").trim();
        try {
            int userId =JwtUtil.validateTokenAndGetUserId(token);
            ctx.attribute("userId", userId);
        } catch(Exception e){
            ctx.status(401).result("Invalid Token");
        }
    }
}