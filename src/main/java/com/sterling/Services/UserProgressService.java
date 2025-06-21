package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.UserProgressDAOInterface;
import com.sterling.Models.UserProgress;

public class UserProgressService {
    private final UserProgressDAOInterface userProgressDAO;

    public UserProgressService(UserProgressDAOInterface userProgressDAO){
        this.userProgressDAO = userProgressDAO;
    }

    public void addUserProgress(UserProgress progress){
        userProgressDAO.addUserProgress(progress);
    }

    public List<UserProgress> getProgressByUserId(int userId){
        return userProgressDAO.getProgressByUserId(userId);
    }

    public boolean deleteProgressByUserId(int userId){
        return userProgressDAO.deleteProgressByUserId(userId);
    }
}
