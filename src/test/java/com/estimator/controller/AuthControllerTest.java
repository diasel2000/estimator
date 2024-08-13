package com.estimator.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.estimator.model.User;
import com.estimator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AuthController authController;

    private final ByteArrayOutputStream logOutput = new ByteArrayOutputStream();
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(logOutput));
    }

    @Test
    public void testGetLoginPage() {
        // Act
        String viewName = authController.getLoginPage();

        // Assert
        assertEquals("login", viewName);
    }

    @Test
    public void testGetRegisterPage() {
        // Act
        String viewName = authController.getRegisterPage();

        // Assert
        assertEquals("register", viewName);
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String password = "password";
        User existingUser = new User();
        existingUser.setEmail(email);

        when(userService.findByEmail(email)).thenReturn(existingUser);

        // Act
        String viewName = authController.registerUser(username, email, password, redirectAttributes);

        // Assert
        assertEquals("redirect:/login", viewName);
        verify(redirectAttributes, times(1)).addFlashAttribute("error", "Email already exists");
        verify(userService, never()).registerUser(anyString(), anyString(), anyString(), anyString());

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Registration failed: Email already exists for email: " + email));
    }

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String password = "password";

        when(userService.findByEmail(email)).thenReturn(null); // Email does not exist

        // Act
        String viewName = authController.registerUser(username, email, password, redirectAttributes);

        // Assert
        assertEquals("redirect:/login", viewName);
        verify(userService, times(1)).registerUser(eq(username), eq(email), eq(password), anyString());
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Registration successful");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Registration successful for user with email: " + email));
    }
}
