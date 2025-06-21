package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.UserProgress;

public interface UserProgressDAOInterface {
    void addUserProgress(UserProgress progress);
    List<UserProgress> getProgressByUserId(int userId);
    boolean deleteProgressByUserId(int userId);
}
