package com.estimator.services;

import static org.junit.jupiter.api.Assertions.*;

import com.estimator.exception.CustomException;
import com.estimator.model.Role;
import com.estimator.model.UserRole;
import com.estimator.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import com.estimator.repository.UserRepository;
import com.estimator.model.User;

import static org.mockito.Mockito.*;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Logger logger = (Logger) LoggerFactory.getLogger(UserDetailsServiceImpl.class);
        logger.addAppender(mockAppender);
    }

    @Test
    void testLoadUserByUsername_UserFound_WithRoles() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);

        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(userRoleRepository.findByUser(user)).thenReturn(userRoles);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRoleRepository, times(1)).findByUser(user);

        // Verify logging
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals(Level.DEBUG, captorLoggingEvent.getAllValues().get(0).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(0).getFormattedMessage().contains("Loading user by username: testuser"));

        assertEquals(Level.INFO, captorLoggingEvent.getAllValues().get(1).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(1).getFormattedMessage().contains("User found with username: testuser"));
    }

    @Test
    void testLoadUserByUsername_UserFound_NoRoles() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        List<UserRole> userRoles = new ArrayList<>();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(userRoleRepository.findByUser(user)).thenReturn(userRoles);

        // Act & Assert
        CustomException.UserRoleNotFoundException thrownException = assertThrows(CustomException.UserRoleNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals("No roles found for user: testuser", thrownException.getMessage());

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRoleRepository, times(1)).findByUser(user);

        // Verify logging
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> logEvents = captorLoggingEvent.getAllValues();

        assertTrue(logEvents.stream().anyMatch(event ->
                event.getLevel().equals(Level.DEBUG) &&
                        event.getFormattedMessage().contains("Loading user by username: testuser")
        ));

        assertTrue(logEvents.stream().anyMatch(event ->
                event.getLevel().equals(Level.WARN) &&
                        event.getFormattedMessage().contains("No roles found for user with username: testuser")
        ));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        CustomException.UserNotFoundException exception = assertThrows(CustomException.UserNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals("User not found with email: " + username, exception.getMessage());
        assertEquals("Username: " + username, exception.getContextInfo());
        verify(userRepository, times(1)).findByUsername(username);

        // Verify logging
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals(Level.DEBUG, captorLoggingEvent.getAllValues().get(0).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(0).getFormattedMessage().contains("Loading user by username: nonexistentuser"));

        assertEquals(Level.WARN, captorLoggingEvent.getAllValues().get(1).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(1).getFormattedMessage().contains("User not found with username: nonexistentuser"));
    }
}

