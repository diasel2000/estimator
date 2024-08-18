package com.estimator.controller;

import com.estimator.dto.RoleDTO;
import com.estimator.dto.SubscriptionDTO;
import com.estimator.dto.UserDTO;
import com.estimator.dto.UserRoleDTO;
import com.estimator.facade.AuthFacade;
import com.estimator.facade.UserFacade;
import com.estimator.model.User;
import com.estimator.services.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserControllerTest {

    @Mock
    private UserFacade userFacade;

    @Mock
    private AuthFacade authFacade;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserController userController;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrentUserLogs() {
        String token = "valid-token";
        String email = "test@example.com";

        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        when(authFacade.getUserByEmail(email)).thenReturn(user);
        when(userFacade.userToUserDTO(user)).thenReturn(userDTO);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<UserDTO> response = userController.getCurrentUser("Bearer " + token);

        assertEquals(ResponseEntity.ok(userDTO), response);
        verify(jwtTokenProvider, times(1)).validateToken(token);
        verify(authFacade, times(1)).getUserByEmail(email);
        verify(userFacade, times(1)).userToUserDTO(user);
    }

    @Test
    public void testUpdateSubscriptionLogs() {
        String email = "test@example.com";
        String subscriptionName = "Basic";

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);

        // Create and set up UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail(email);
        userDTO.setGoogleID("google-id");
        userDTO.setCreatedAt(LocalDateTime.now());

        // Set up SubscriptionDTO and UserRoleDTO if needed
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setSubscriptionName(subscriptionName);
        userDTO.setSubscription(subscriptionDTO);

        UserRoleDTO userRoleDTO = new UserRoleDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("USER");
        userRoleDTO.setRole(roleDTO);
        userDTO.setUserRoles(Set.of(userRoleDTO));

        when(authFacade.getUserByUsername(email)).thenReturn(user);
        when(userFacade.updateSubscription(email, subscriptionName)).thenReturn(user);
        when(userFacade.userToUserDTO(user)).thenReturn(userDTO);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<?> response = userController.updateSubscription(subscriptionName, principal);

        assertEquals(ResponseEntity.ok(userDTO), response);
        verify(authFacade, times(1)).getUserByUsername(email);
        verify(userFacade, times(1)).updateSubscription(email, subscriptionName);
        verify(userFacade, times(1)).userToUserDTO(user);

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Updating subscription for user with email: " + email));
    }


    @Test
    public void testDeleteUserLogs() {
        String email = "test@example.com";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        User authenticatedUser = new User();
        authenticatedUser.setEmail(email);

        when(authFacade.getUserByUsername(email)).thenReturn(authenticatedUser);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<?> response = userController.deleteUser(email, principal);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(userFacade, times(1)).deleteUser(email);

        // Verify logging
        assertTrue(appender.getLogMessages().contains("Deleting user with email: " + email));
    }

    @Test
    public void testGetCurrentUserNotFoundLogs() {
        String token = "valid-token";
        String email = "notfound@example.com";

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        when(authFacade.getUserByEmail(email)).thenReturn(null);

        // Mock logger to capture log output
        TestAppender appender = new TestAppender();
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);
        appender.start();

        ResponseEntity<UserDTO> response = userController.getCurrentUser("Bearer " + token);

        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), response);
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


