package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.GymBuddyDAOInterface;
//import com.sterling.Models.GymBuddy;
import com.sterling.Models.User;

public class GymBuddyService {
    private GymBuddyDAOInterface gymBuddyDao;

    public GymBuddyService(GymBuddyDAOInterface gymBuddyDao){
        this.gymBuddyDao = gymBuddyDao;
    }

    public void addGymBuddy(int userId, int buddyId){
        gymBuddyDao.addGymBuddy(userId, buddyId);
    }

    public boolean exists(int userId, int buddyId){
        return gymBuddyDao.exists(userId, buddyId);
    }

    public List<User> getGymBuddiesByUserId(int userId){
        return gymBuddyDao.getBuddiesByUserId(userId);
    }
}
