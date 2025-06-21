package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.UserDAOInterface;
import com.sterling.Models.User;

public class UserService {
    private final UserDAOInterface userDao;

    public UserService(UserDAOInterface userDAO){
        this.userDao = userDAO;
    }

    public boolean registerUser(User user){
        User exists = userDao.getUserByUsername(user.getUsername());
        
        if(exists != null)
            return false;

        userDao.addUser(user);
        return true;
    }

    public User loginUser(String username, String password){
        User user = userDao.getUserByUsername(username);

        if(user != null && user.getPassword().equals(password))
            return user;
        return null;
    }

    public User getUserById(int id){
        return userDao.getUserById(id);
    }

    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    public boolean updateUser(User user){
        //User existingUser = userDao.getUserById(user.getId());

        user.setAge(user.getAge());
        user.setCurrentBodyFatPercentage(user.getCurrentBodyFatPercentage());
        user.setCurrentWeight(user.getCurrentWeight());
        user.setEmail(user.getEmail());
        user.setFirstName(user.getFirstName());
        user.setHeightFeet(user.getHeightFeet());
        user.setHeightInches(user.getHeightInches());
        user.setLastName(user.getLastName());
        user.setPassword(user.getPassword());
        user.setStartBodyFatPercentage(user.getStartBodyFatPercentage());
        user.setStartWeight(user.getStartWeight());
        user.setUsername(user.getUsername());

        return userDao.updateUser(user);
    }

    public boolean deleteUser(int id){
        return userDao.deleteUser(id);
    }

    public boolean updateUserRole(int userId, String role){
        return userDao.updateUserRole(userId, role);
    }

    public boolean assignClientToTrainer(int clientId, int trainerId){
        return userDao.assignClientToTrainer(clientId, trainerId);
    }

    public List<User> getClientsByTrainerId(int trainerId){
        return userDao.getClientsByTrainerId(trainerId);
    }
}
