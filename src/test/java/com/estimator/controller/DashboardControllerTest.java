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

public class DashboardControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDashboardWithBasicSubscription() {
        String username = "testuser";
        User user = new User();
        user.setSubscription(new Subscription("Basic"));

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        String viewName = dashboardController.dashboard(model, authentication);

        assertEquals("dashboardB", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "Basic");
    }

    @Test
    public void testDashboardWithStandardSubscription() {
        String username = "testuser";
        User user = new User();
        user.setSubscription(new Subscription("Standard"));

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        String viewName = dashboardController.dashboard(model, authentication);

        assertEquals("dashboardS", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "Standard");
    }

    @Test
    public void testDashboardWithUltimateSubscription() {
        String username = "testuser";
        User user = new User();
        user.setSubscription(new Subscription("Ultimate"));

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        String viewName = dashboardController.dashboard(model, authentication);

        assertEquals("dashboardU", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "Ultimate");
    }

    @Test
    public void testDashboardWithNoSubscription() {
        String username = "testuser";
        User user = new User();
        user.setSubscription(null);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(userService.findByGoogleID(username)).thenReturn(user);

        String viewName = dashboardController.dashboard(model, authentication);

        assertEquals("dashboard", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("roles", user.getRoles());
        verify(model, times(1)).addAttribute("subscriptionName", "No Subscription");
    }

    @Test
    public void testDashboardWithNoUser() {
        String username = "testuser";

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(null);
        when(userService.findByGoogleID(username)).thenReturn(null);

        String viewName = dashboardController.dashboard(model, authentication);

        assertEquals("error", viewName);
    }
}
