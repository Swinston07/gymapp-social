package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.User;

public interface UserDAOInterface {
    void addUser(User user);
    User getUserById(int id);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
    boolean updateUserRole(int userId, String role);        //Admin only
    boolean assignClientToTrainer(int clientId, int trainerId); //Admin and Trainer only
    List<User> getClientsByTrainerId(int trainerId);           //Trainer Specific
    boolean updateHomeGym(int userId, String homeGym, Double latitude, Double longitude);
    boolean toggleWorkoutStatus(int userId);
    List<User> findMatchingUsers(String homeGym, String role, int userId);
    List<User> findUsersByHomeGym(String homeGym, int userId);
    List<User> findUsersByHomeGymAndRole(String homeGym, String role, int userId);
}
