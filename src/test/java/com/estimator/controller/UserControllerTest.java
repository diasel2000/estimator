package com.estimator.controller;

import com.estimator.dto.RegisterRequest;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.model.dto.RegisterRequest;
import com.estimator.services.UserService;
import com.estimator.services.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private Model model;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserController userController;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserLogs() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setGoogleId("google-id");

        User user = new User();
        when(userService.registerUser(anyString(), anyString(), anyString(), anyString())).thenReturn(user);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<User> response = userController.registerUser(request);

        assertEquals(ResponseEntity.ok(user), response);
        verify(userService, times(1)).registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getGoogleId());

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Registering user with email: test@example.com"));
        assertTrue(appender.getLogMessages().contains("User registered successfully with email: test@example.com"));
    }

    @Test
    public void testGetProfilePageLogs() {
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");

        User user = new User();
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        Model model = mock(Model.class);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        String viewName = userController.getProfilePage(model, authentication);

        assertEquals("profile", viewName);
        verify(model, times(1)).addAttribute("user", user);

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Getting profile page for user"));
    }

    @Test
    public void testUpdateSubscriptionLogs() {
        User user = new User();
        Subscription subscription = new Subscription();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(subscriptionService.getSubscriptionByName("Basic")).thenReturn(subscription);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<User> response = userController.updateSubscription("Basic", principal);

        assertEquals(ResponseEntity.ok(user), response);
        verify(userService, times(1)).updateSubscription(user, subscription);

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Updating subscription for user with email: test@example.com"));
    }

    @Test
    public void testDeleteUserLogs() {
        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<Void> response = userController.deleteUser("test@example.com");

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(userService, times(1)).deleteUserByEmail("test@example.com");

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Deleting user with email: test@example.com"));
    }

    // Additional test for user not found logging
    @Test
    public void testGetProfilePageUserNotFoundLogs() {
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("notfound@example.com");

        when(userService.findByEmail("notfound@example.com")).thenReturn(null);

        Model model = mock(Model.class);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getProfilePage(model, authentication);
        });

        assertEquals("User not found", thrown.getMessage());

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Getting profile page for user"));
        assertTrue(appender.getLogMessages().contains("User not found"));
    }

    @Test
    public void testGetProfilePageWithUserDetailsLogs() {
        // Mock Authentication and UserDetails
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");

        // Mock UserService to return a User
        User user = new User();
        when(userService.findByUserName("testuser")).thenReturn(user);

        Model model = mock(Model.class);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        // Call the method
        String viewName = userController.getProfilePage(model, authentication);

        // Assertions
        assertEquals("profile", viewName);
        verify(model, times(1)).addAttribute("user", user);

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Getting profile page for user"));
        assertFalse(appender.getLogMessages().contains("User not found"));  // Ensure this log does not appear
    }


    // TestAppender class for capturing log output
    public static class TestAppender extends ch.qos.logback.core.AppenderBase<ch.qos.logback.classic.spi.ILoggingEvent> {
        private final List<String> logMessages = new ArrayList<>();

        @Override
        protected void append(ch.qos.logback.classic.spi.ILoggingEvent eventObject) {
            logMessages.add(eventObject.getFormattedMessage());
        }

        public List<String> getLogMessages() {
            return logMessages;
        }
    }
}


