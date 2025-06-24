package com.sterling.Controllers;

import java.util.List;
import java.util.Map;

import com.sterling.Models.User;
import com.sterling.Services.UserService;
import com.sterling.Utils.JwtUtil;

import io.javalin.http.Context;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    public void registerUser(Context ctx){
        User user = ctx.bodyAsClass(User.class);
        boolean success = userService.registerUser(user);

        user.setHomeGym(ctx.formParam("homeGym"));
        String latStr = ctx.formParam("latitude");
        String lonStr = ctx.formParam("longitude");

        if(latStr != null) user.setLatitude(Double.parseDouble(latStr));
        if(lonStr != null) user.setLongitude(Double.parseDouble(lonStr));


        if(success)
            ctx.status(201).result("User registered successfully");
        else
            ctx.status(409).result("Email or username is already in use");
    }

    public void loginUser(Context ctx){
        User loginRequest = ctx.bodyAsClass(User.class);

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.loginUser(username, password);

        if(user != null){
            String token = JwtUtil.generateToken(user.getId());
            ctx.json(Map.of(
                "token", token,
                "user", user
                ));
        }
        else
            ctx.status(401).result("Invalid username or password");
    }

    public void getUserById(Context ctx){
       int id = Integer.parseInt(ctx.pathParam("id"));
       User user = userService.getUserById(id);

       if(user != null)
            ctx.json(user);
        else
            ctx.status(404).result("User not found");
    }

    public void getAllUsers(Context ctx){
        // int requesterId = ctx.attribute("userId");
        // User requester = userService.getUserById(requesterId);

        // if(!"admin".equalsIgnoreCase(requester.getRole())){
        //     ctx.status(403).result("Only admin can view all users");
        //     return;
        // }
        
        List<User> users = userService.getAllUsers();
        ctx.json(users);
    }

    public void updateUser(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        User updatedUser = ctx.bodyAsClass(User.class);
        updatedUser.setHomeGym(ctx.formParam("homeGym"));
        String latStr = ctx.formParam("latitude");
        String lonStr = ctx.formParam("longitude");

        updatedUser.setId(id);

        if(latStr != null) updatedUser.setLatitude(Double.parseDouble(latStr));
        if(lonStr != null) updatedUser.setLongitude(Double.parseDouble(lonStr));

        boolean success = userService.updateUser(updatedUser);

        if(success)
            ctx.status(200).result("User updated");
        else
            ctx.status(404).result("User not found");
    }

    public void deleteUser(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = userService.deleteUser(id);

        if(deleted)
            ctx.status(200).result("User deleted");
        else
            ctx.status(404).result("User not found");
    }

    public void updateUserRole(Context ctx){
        int userId = Integer.parseInt(ctx.pathParam("id"));
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String newRole = body.get("role");
        int requesterId = ctx.attribute("userId");

        User requester = userService.getUserById(requesterId);

        if(!"admin".equals(requester.getRole())){
            ctx.status(403).result("Only admin can update user roles.");
            return;
        }

        boolean success = userService.updateUserRole(userId, newRole);

        if(success){
            ctx.status(200).result("User role updated");
        }
        else{
            ctx.status(404).result("User not found or update failed");
        }
    }

    public void assignClientToTrainer(Context ctx){
        int requesterId = ctx.attribute("userId");
        int trainerId = Integer.parseInt(ctx.pathParam("trainerId"));
        int clientId= Integer.parseInt(ctx.pathParam("clientId"));

        User requester = userService.getUserById(requesterId);
        User trainer = userService.getUserById(trainerId);
        User client = userService.getUserById(clientId);

        if(requester==null || trainer == null || client == null){
            ctx.status(404).result("User not found");
            return;
        }

        boolean isAdmin = requester.getRole().equalsIgnoreCase("admin");
        boolean isSelfAssignment = requester.getRole().equalsIgnoreCase("trainer") && requesterId == trainerId;

        if(!(isAdmin || isSelfAssignment)){
            ctx.status(403).result("Unauthorized to assign client to this trainer.");
            return;
        }

        if(!client.getRole().equalsIgnoreCase("client")){
            ctx.status(404).result("Selected user is not a client");
            return;
        }

        boolean success = userService.assignClientToTrainer(clientId, trainerId);

        if(success){
            ctx.status(200).result("Client assigned to trainer successfully");
        }
        else{
            ctx.status(500).result("Failed to assign client");
        }
    }

    public void getClientsByTrainerId(Context ctx){
        int requesterId = ctx.attribute("userId");
        int trainerId = Integer.parseInt(ctx.pathParam("trainerId"));

        User requester = userService.getUserById(requesterId);
        User trainer = userService.getUserById(trainerId);

        if(requester == null || trainer == null){
            ctx.status(404).result("User not found");
            return;
        }

        boolean isAdmin = requester.getRole().equalsIgnoreCase("admin");
        boolean isTrainerSelf = requester.getRole().equalsIgnoreCase("trainer") && requesterId==trainerId;

        if(!(isAdmin || isTrainerSelf)){
            ctx.status(403).result("Unauthorized to view clients for this trainer.");
            return;
        }

        List<User> clients = userService.getClientsByTrainerId(trainerId);

        if(clients.isEmpty()){
            ctx.status(404).result("No clients assigned to this trainer");
        }
        else{
            ctx.json(clients);
        }
    }

    public void getNearByGyms(Context ctx){
        try {
            int userId = Integer.parseInt(ctx.pathParam("id"));
            double latitude = Double.parseDouble(ctx.queryParam("latitude"));
            double longitude = Double.parseDouble(ctx.queryParam("longitude"));
            User user = userService.getUserById(userId);
            
            if(user == null){
                ctx.status(404).result("User not found");
                return;
            }

            String gymResults = userService.getNearByGyms(latitude, longitude);
            ctx.status(200).result(gymResults);
        } catch (Exception e) {
            ctx.status(500).result("Failed to retrieve any nearby gyms");
            e.printStackTrace();
        }
    }

    public void updateHomeGym(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));

        if(requesterId != userId){
            ctx.status(404).result("Unauthorized to updat another user;s location");
            return;
        }
        
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String homeGym = (String) body.get("home_gym");
        Double lat = (Double) body.get("latitude");
        Double lon = (Double) body.get("longitude");

        boolean success = userService.updateHomeGym(userId, homeGym, lat, lon);

        if(success) {
            ctx.status(200).result("Home gym updated");
        } else {
            ctx.status(400).result("Failed to updated Home gym");
        }
    }

    public void toggleWorkoutStatus(Context ctx){
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int requesterId = ctx.attribute("userId");

        if(requesterId != userId) {
            ctx.status(403).result("Not authroized to update another user's workout status");
            return;
        }

        boolean success = userService.toggleWorkoutStatus(userId);

        if(success){
            ctx.status(200).result("Workout status toggles successufully");
        } else{
            ctx.status(500).result("Failed to toggle workout status");
        }
    }

    public void findMatchingUsers(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        String role = ctx.queryParam("role");

        if(requesterId != userId){
            ctx.status(403).result("unauthorized");
        }

        List<User> matches = userService.findMatchingUsers(role, userId);
        ctx.json(matches);
    }
}
