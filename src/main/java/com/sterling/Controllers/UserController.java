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
        updatedUser.setId(id);

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
}
