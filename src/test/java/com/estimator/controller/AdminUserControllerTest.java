package com.estimator.controller;

import com.estimator.dto.UserDTO;
import com.estimator.facade.UserFacade;
import com.estimator.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

public class AdminUserControllerTest {

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private AdminUserController adminUserController;

    private final ByteArrayOutputStream logOutput = new ByteArrayOutputStream();
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(logOutput));
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        List<UserDTO> userDTOs = Arrays.asList(new UserDTO(), new UserDTO());

        when(userFacade.getAllUsers()).thenReturn(users);
        when(userFacade.userToUserDTO(user1)).thenReturn(userDTOs.get(0));
        when(userFacade.userToUserDTO(user2)).thenReturn(userDTOs.get(1));

        // Act
        ResponseEntity<List<UserDTO>> response = adminUserController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOs, response.getBody());

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Managed users - Total users: 2"));
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        Long userId = 1L;
        when(userFacade.existsById(userId)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> response = adminUserController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody().get("message"));
        verify(userFacade, times(1)).deleteUserById(userId);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Deleted user with ID: 1"));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userFacade.existsById(userId)).thenReturn(false);

        // Act
        ResponseEntity<Map<String, String>> response = adminUserController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().get("error"));
        verify(userFacade, times(0)).deleteUserById(userId);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Attempted to delete user with ID: 1 - User not found"));
    }
}
