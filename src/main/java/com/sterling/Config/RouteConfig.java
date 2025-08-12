package com.sterling.Config;

import com.sterling.Controllers.*;
import io.javalin.Javalin;

import java.util.Map;

public class RouteConfig {

    public static void registerRoutes(Javalin app, Map<String, Object> beans) {
        UserController userController = (UserController) beans.get("userController");
        ExerciseController exerciseController = (ExerciseController) beans.get("exerciseController");
        AssignedWorkoutController assignedWorkoutController = (AssignedWorkoutController) beans.get("assignedWorkoutController");
        AssignedExerciseController assignedExerciseController = (AssignedExerciseController) beans.get("assignedExerciseController");
        BlogPostController blogPostController = (BlogPostController) beans.get("blogPostController");
        UserProgressController userProgressController = (UserProgressController) beans.get("userProgressController");
        WorkoutInviteController workoutInviteController = (WorkoutInviteController) beans.get("workoutInviteController");
        GymBuddyController gymBuddyController = (GymBuddyController) beans.get("gymBuddyController");
        MessageController messageController = (MessageController) beans.get("messageController");
        PhotoController photoController = (PhotoController) beans.get("photoController");
        ReviewController reviewController = (ReviewController) beans.get("reviewController");
        WorkoutSessionController workoutSessionController = (WorkoutSessionController) beans.get("workoutSessionController");
        DeviceTokenController deviceTokenController = (DeviceTokenController) beans.get("deviceTokenController");
        NotificationController notificationController = (NotificationController) beans.get("notificationController");
        UnreadController unreadController = (UnreadController) beans.get("unreadController");

        // ===== User Routes =====
        app.post("/users", userController::registerUser);
        app.post("/login", userController::loginUser);
        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.get("/trainers/{trainerId}/clients", userController::getClientsByTrainerId);
        app.get("/users/{id}/nearby-gyms", userController::getNearByGyms);
        app.get("/users/{id}/matches", userController::findUsersByHomeGym);
        app.get("/users/{id}/matches/filter", userController::findUsersByFilters);
        app.put("/users/{id}", userController::updateUser);
        app.put("/users/{id}/role", userController::updateUserRole);
        app.put("/users/{id}/update-location", userController::updateHomeGym);
        app.put("/users/{id}/toggle-workout", userController::toggleWorkoutStatus);
        app.put("/trainers/{trainerId}/clients/{clientId}", userController::assignClientToTrainer);
        app.delete("/users/{id}", userController::deleteUser);

        // ===== Exercise Routes =====
        app.post("/users/{id}/exercises", exerciseController::addExercise);
        app.get("/exercises", exerciseController::getAllExercises);
        app.get("/exercises/{id}", exerciseController::getExerciseById);
        app.get("/users/{id}/exercises", exerciseController::getExercisesByUserId);
        app.put("/exercises/{id}", exerciseController::updateExercise);
        app.delete("/exercises/{id}", exerciseController::deleteExercise);

        // ===== Assigned Workout Routes =====
        app.post("/trainers/{trainerId}/clients/{clientId}/assigned-workouts", assignedWorkoutController::assignWorkout);
        app.get("/assigned-workouts/client/{clientId}", assignedWorkoutController::getWorkoutsByClientId);
        app.get("/assigned-workouts/trainer/{trainerId}", assignedWorkoutController::getWorkoutsByTrainerId);
        app.get("/assigned-workouts/client/{clientId}/date/{date}",assignedWorkoutController::getWorkoutsByClientIdAndDate);
        app.put("/assigned-workouts/{assignmentId}/complete", assignedWorkoutController::markWorkoutCompleted);
        app.delete("/assigned-workouts/{assignmentId}", assignedWorkoutController::deleteAssignment);

        // ===== Assigned Exercises Routes =====
        app.post("/assigned-exercises/{assignmentId}", assignedExerciseController::addAssignedExercise);
        app.get("/assigned-exercises/{assignmentId}", assignedExerciseController::getAssignedExercisesByAssignmentId);
        app.put("/assigned-exercises/{assignedExerciseId}", assignedExerciseController::updateAssignedExercise);
        app.put("/assigned-exercises/{assignedExerciseId}/completed", assignedExerciseController::markExerciseCompleted);
        app.delete("/assigned-exercises/{assignedExerciseId}", assignedExerciseController::deleteAssignedExercise);

        // ===== Blog Post Routes =====
        app.post("/users/{userId}/blog-posts", blogPostController::createPost);
        app.get("/blog-posts", blogPostController::getAllBlogPosts);
        app.get("/blog-posts/{postId}", blogPostController::getPostById);
        app.get("/users/{userId}/blog-posts", blogPostController::getPostsByUserId);
        app.put("/blog-posts/{postId}", blogPostController::updatePost);
        app.delete("/blog-posts/{postId}", blogPostController::deletePost);

        // ===== Progress Routes =====
        app.post("/progress/{userId}", userProgressController::addUserProgress);
        app.get("/progress/{userId}", userProgressController::getProgressByUserId);
        app.delete("/progress/{userId}", userProgressController::deleteProgressByUserId);

        // ===== Workout Invite Routes =====
        app.post("/users/{id}/workout-invites/{recipientId}", workoutInviteController::sendInvite);
        app.get("/users/{id}/workout-invites", workoutInviteController::getInvitesForUser);
        app.put("/users/{id}/workout-invites/{inviteId}", workoutInviteController::updateInviteStatus);

        // ===== Gym Buddy Routes =====
        app.post("/users/{id}/gym-buddies/{buddyId}", gymBuddyController::addGymBuddy);
        app.get("/users/{id}/gym-buddies", gymBuddyController::getGymBuddiesByUserId);
        app.get("/users/{id}/gym-buddies/{buddyId}", gymBuddyController::exists);

        // ===== Message Routes =====
        app.post("/messages/{id}/{receiverId}", messageController::sendMessage);
        app.get("/messages/{id}/{otherUserId}", messageController::getMessagesBetweenUsers);
        app.get("/messages/{id}", messageController::getMessagesForUser);
        app.delete("/messages/{messageId}", messageController::deleteMessage);

        // ===== Photo Routes =====
        app.post("/users/{id}/photos", photoController::uploadPhoto);
        app.get("/users/{id}/photos", photoController::getPhotosByUserId);
        app.get("/photos/{photoId}", photoController::getPhotoByPhotoId);
        app.delete("/photos/{photoId}", photoController::deletePhotoByPhotoId);

        // ===== Review Routes =====
        app.post("/users/{id}/reviews", reviewController::createReview);
        app.get("/users/{id}/reviews", reviewController::getReviewsForUser);
        app.get("/users/{id}/reviews/written", reviewController::getReviewsWrittenByUser);
        app.get("/users/{id}/reviews/average", reviewController::getAverageRatingForUser);
        app.delete("/users/{id}/delete/{reviewId}", reviewController::deleteReview);

        // ===== Workout Session Routes =====
        app.post("/users/{id}/sessions", workoutSessionController::createSession);
        app.get("/users/{id}/sessions", workoutSessionController::getSessionsByUserId);
        app.get("/sessions/{sessionId}", workoutSessionController::getSessionById);
        app.put("/sessions/{sessionId}/status/{status}", workoutSessionController::updateSessionStatus);
        app.delete("/sessions/{sessionId}", workoutSessionController::deleteSession);

        // ===== Stripe Routes (Static Methods) =====
        app.post("/create-checkout-session", StripeController::createCheckoutSession);
        app.post("/stripe/webhook", StripeController::handleWebhook);
        app.post("/create-billing-portal-session", StripeController::createBillingPortal);
        app.get("/subscriptions/{id}", StripeController::getAllUserSubscriptions);
        app.delete("/subscriptions/{id}", StripeController::cancelSubscription);

        // ===== Device Token Routes =====
        app.post("/users/{id}/devices", deviceTokenController::register);
        app.delete("/users/{id}/devices", deviceTokenController::revoke);
        app.get("/users/{id}/devices", deviceTokenController::list);

        // ===== Push Notifications Routes =====
        app.get("/users/{id}/notifications", notificationController::list);
        app.post("/users/{id}/notifications/{notificationId}/read", notificationController::markRead);
        app.post("/users/{id}/notifications/mark-all-read", notificationController::markAllRead);

        // ===== Unread / Badge Routes =====
        app.get ("/users/{id}/unread-summary",              unreadController::summary);
        app.get("/users/{id}/messages/unread-by-partner", unreadController::unreadByPartner);
        app.post("/users/{id}/sections/{section}/seen",     unreadController::markSectionSeen);
        app.post("/users/{id}/messages/read/{otherUserId}", unreadController::markMessagesRead);
    }
}
