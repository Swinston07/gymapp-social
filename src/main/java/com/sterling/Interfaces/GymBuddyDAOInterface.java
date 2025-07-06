package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.GymBuddy;

public interface GymBuddyDAOInterface {
    void addGymBuddy(int userId, int buddyId);
    boolean exists(int userId, int buddyId);
    List<GymBuddy> getBuddiesByUserId(int userId);
}
