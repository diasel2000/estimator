package com.estimator.controller;

import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DashboardController dashboardController;

    private final ByteArrayOutputStream logOutput = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(logOutput));  // Capture log output
    }

    @Test
    public void testDashboardWithBasicSubscription() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setSubscription(new Subscription("Basic"));
        user.setUsername(username);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        // Act
        String viewName = dashboardController.dashboard(model, authentication);

        // Assert
        assertEquals("dashboardB", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "Basic");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: Basic"));
        assertTrue(logContent.contains("Redirecting to Basic dashboard."));
        assertTrue(logContent.contains("User found. Username: testuser"));
    }

    @Test
    public void testDashboardWithStandardSubscription() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setSubscription(new Subscription("Standard"));

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        // Act
        String viewName = dashboardController.dashboard(model, authentication);

        // Assert
        assertEquals("dashboardS", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "Standard");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: Standard"));
        assertTrue(logContent.contains("Redirecting to Standard dashboard."));
        assertTrue(logContent.contains("User found. Username: testuser"));
    }

    @Test
    public void testDashboardWithUltimateSubscription() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setSubscription(new Subscription("Ultimate"));
        user.setUsername(username);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        // Act
        String viewName = dashboardController.dashboard(model, authentication);

        // Assert
        assertEquals("dashboardU", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "Ultimate");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: Ultimate"));
        assertTrue(logContent.contains("Redirecting to Ultimate dashboard."));
        assertTrue(logContent.contains("User found. Username: testuser"));
    }

    @Test
    public void testDashboardWithNoSubscription() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setSubscription(null);
        user.setUsername(username);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        // Act
        String viewName = dashboardController.dashboard(model, authentication);

        // Assert
        assertEquals("dashboard", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "No Subscription");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: No Subscription"));
        assertTrue(logContent.contains("Redirecting to default dashboard."));
        assertTrue(logContent.contains("User found. Username: testuser"));
    }

    @Test
    public void testDashboardWithNoUser() {
        // Arrange
        String username = "testuser";

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(null);
        when(userService.findByGoogleID(username)).thenReturn(null);

        // Act
        String viewName = dashboardController.dashboard(model, authentication);

        // Assert
        assertEquals("error", viewName);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User not found. Redirecting to error page."));
    }
}
