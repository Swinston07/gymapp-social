package com.sterling.Services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Interfaces.UserDAOInterface;
import com.sterling.Models.Consistency;
import com.sterling.Models.ExperienceLevel;
import com.sterling.Models.Lifestyle;
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
        User existingUser = userDao.getUserById(user.getId());

        if(user.getAge()!=0) existingUser.setAge(user.getAge());
        if(user.getCurrentBodyFatPercentage()!=0) existingUser.setCurrentBodyFatPercentage(user.getCurrentBodyFatPercentage());
        if(user.getCurrentWeight()!=0) existingUser.setCurrentWeight(user.getCurrentWeight());
        if(user.getEmail()!=null) existingUser.setEmail(user.getEmail());
        if(user.getFirstName()!=null) existingUser.setFirstName(user.getFirstName());
        if(user.getHeightFeet()!=0) existingUser.setHeightFeet(user.getHeightFeet());
        if(user.getHeightInches()!=0) existingUser.setHeightInches(user.getHeightInches());
        if(user.getRole()!=null) existingUser.setRole(user.getRole());
        if(user.getLastName()!=null) existingUser.setLastName(user.getLastName());
        if(user.getPassword()!=null) existingUser.setPassword(user.getPassword());
        if(user.getStartBodyFatPercentage()!=0) existingUser.setStartBodyFatPercentage(user.getStartBodyFatPercentage());
        if(user.getStartWeight()!=0) existingUser.setStartWeight(user.getStartWeight());
        if(user.getUsername()!=null) existingUser.setUsername(user.getUsername());
        if(user.getHomeGym()!=null) existingUser.setHomeGym(user.getHomeGym());
        if(user.getLatitude()!=null) existingUser.setLatitude(user.getLatitude());
        if(user.getLongitude()!=null) existingUser.setLongitude(user.getLongitude());
        if(user.getAboutMe()!=null) existingUser.setAboutMe(user.getAboutMe());
        if(user.getExperienceLevel()!=null) existingUser.setExperienceLevel(user.getExperienceLevel());
        if(user.getLifestyle()!=null) existingUser.setLifestyle(user.getLifestyle());
        if(user.getConsistency()!=null) existingUser.setConsistency(user.getConsistency());

        return userDao.updateUser(existingUser);
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

    private static final String API_KEY = System.getenv("HERE_API_KEY");

    public String getNearByGyms(double latitude, double longitude){
        try {
            String query = URLEncoder.encode("gym", "UTF-8");
            String urlString = String.format(
                "https://discover.search.hereapi.com/v1/discover?q=%s&at=%f,%f&apiKey=%s",
                 query, latitude, longitude, API_KEY
            );

           System.out.println("\n\n========== REQUEST URL ==========");
            System.out.println(urlString);
            System.out.println("=================================\n\n");

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            conn.disconnect();

            return response.toString();

        } catch (Exception e){
            e.printStackTrace();
            return "{\"error\":\"Failed to fetch gyms\"}";
        }
    }

    public boolean updateHomeGym(int userId, String homeGym, Double latitude, Double longitude){
        return userDao.updateHomeGym(userId, homeGym, latitude, longitude);
    }

    public boolean toggleWorkoutStatus(int userId){
        return userDao.toggleWorkoutStatus(userId);
    }

    public List<User> findUsersByHomeGymAndRole(String role, int userId){
        User requester = userDao.getUserById(userId); 

        if(requester == null || requester.getHomeGym() == null) return new ArrayList<>();

        return userDao.findUsersByHomeGymAndRole(requester.getHomeGym(), role, userId);
    }

    public List<User> findUsersByHomeGym(int userId){
        User requester = userDao.getUserById(userId);

        if(requester == null || requester.getHomeGym() == null) return new ArrayList<>();
        return userDao.findUsersByHomeGym(requester.getHomeGym(), userId);
    }

    public List<User> findUsersByFilters(String homeGym, String role, Integer minAge, Integer maxAge, ExperienceLevel experienceLevel, Lifestyle lifestyle, Consistency consistency, int userId) {
        return userDao.findUsersByFilters(homeGym, role, minAge, maxAge, experienceLevel, lifestyle, consistency, userId);
    }
}
