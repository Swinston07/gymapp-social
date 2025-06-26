package com.sterling.Services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
        user.setHomeGym(user.getHomeGym());
        user.setLatitude(user.getLatitude());
        user.setLongitude(user.getLongitude());

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
}
