package com.estimator.controller;

import com.estimator.facade.DashboardFacade;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class DashboardControllerTest {

    @Mock
    private DashboardFacade dashboardFacade;

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
        when(dashboardFacade.getUserDetails(username)).thenReturn(user);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboard(authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dashboardB", response.getBody().get("dashboardView"));
        verify(dashboardFacade, times(1)).getUserDetails(username);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: Basic"));
        assertTrue(logContent.contains("User found. Username: testuser"));
    }

    @Test
    public void testDashboardWithStandardSubscription() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setSubscription(new Subscription("Standard"));
        user.setUsername(username);

        when(authentication.getName()).thenReturn(username);
        when(dashboardFacade.getUserDetails(username)).thenReturn(user);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboard(authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dashboardS", response.getBody().get("dashboardView"));
        verify(dashboardFacade, times(1)).getUserDetails(username);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: Standard"));
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
        when(dashboardFacade.getUserDetails(username)).thenReturn(user);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboard(authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dashboardU", response.getBody().get("dashboardView"));
        verify(dashboardFacade, times(1)).getUserDetails(username);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: Ultimate"));
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
        when(dashboardFacade.getUserDetails(username)).thenReturn(user);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboard(authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dashboard", response.getBody().get("dashboardView"));
        verify(dashboardFacade, times(1)).getUserDetails(username);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User subscription: No Subscription"));
        assertTrue(logContent.contains("User found. Username: testuser"));
    }

    @Test
    public void testDashboardWithNoUser() {
        // Arrange
        String username = "testuser";

        when(authentication.getName()).thenReturn(username);
        when(dashboardFacade.getUserDetails(username)).thenReturn(null);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboard(authentication);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().get("error"));

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching user details for username: testuser"));
        assertTrue(logContent.contains("User not found. Returning error response."));
    }
}
