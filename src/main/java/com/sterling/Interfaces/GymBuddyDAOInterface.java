package com.sterling.Interfaces;

import java.util.List;
import com.sterling.Models.User;

public interface GymBuddyDAOInterface {
    void addGymBuddy(int userId, int buddyId);
    boolean exists(int userId, int buddyId);
    List<User> getBuddiesByUserId(int userId);
}
