package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sterling.Interfaces.UserDAOInterface;
import com.sterling.Models.User;

public class UserServiceTest {
    UserDAOInterface userDAO;
    UserService userService;

    @BeforeEach
    public void setup(){
        userDAO = mock(UserDAOInterface.class);
        userService = new UserService(userDAO);
    }

    @Test
    void testDeleteUser() {
        when(userDAO.deleteUser(1)).thenReturn(true);

        boolean deleted = userService.deleteUser(1);
        
        assertTrue(deleted);

        verify(userDAO,times(1)).deleteUser(1);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();

        user1.setId(1);
        user1.setUsername("user1");
        user2.setId(2);
        user2.setUsername("user2");

        List<User> mockUserList = List.of(user1, user2);

        when(userDAO.getAllUsers()).thenReturn(mockUserList);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.get(0).getId());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals(2, users.get(1).getId());
        assertEquals("user2", users.get(1).getUsername());

        verify(userDAO,times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        User user = new User();

        user.setId(1);
        user.setUsername("username");

        when(userDAO.getUserById(1)).thenReturn(user);

        User found = userService.getUserById(1);

        assertEquals("username", found.getUsername());
        verify(userDAO,times(1)).getUserById(1);
    }

    @Test
    void testRegisterUser() {
        User user = new User();

        user.setId(1);
        user.setUsername("username");
        user.setEmail("username@gmail.com");

        when(userDAO.getUserByEmail("username@gmail.com")).thenReturn(null);

        boolean success = userService.registerUser(user);

        assertTrue(success);
        verify(userDAO,times(1)).addUser(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User();

        user.setId(1);
        user.setUsername("username");
        user.setEmail("username@gmail.com");

        when(userDAO.updateUser(user)).thenReturn(true);

        boolean updated = userService.updateUser(user);

        assertTrue(updated);
        verify(userDAO,times(1)).updateUser(user);
    }
}
