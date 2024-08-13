package com.estimator.controller;

import com.estimator.model.User;
import com.estimator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

public class AdminUserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

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
    public void testManageUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // Act
        String viewName = adminUserController.manageUsers(model);

        // Assert
        assertEquals("manage_users", viewName);
        verify(model, times(1)).addAttribute("users", users);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Managed users - Total users: 2"));
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        String viewName = adminUserController.deleteUser(userId, redirectAttributes);

        // Assert
        assertEquals("redirect:/admin/users", viewName);
        verify(userRepository, times(1)).deleteById(userId);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "User deleted successfully");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Deleted user with ID: 1"));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        String viewName = adminUserController.deleteUser(userId, redirectAttributes);

        // Assert
        assertEquals("redirect:/admin/users", viewName);
        verify(userRepository, times(0)).deleteById(userId);
        verify(redirectAttributes, times(1)).addFlashAttribute("error", "User not found");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Attempted to delete user with ID: 1 - User not found"));
    }
}
