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
        String email = "testuser@example.com";
        User user = new User();
        user.setUsername("testuser@example.com");
        user.setEmail(email);
        user.setPassword("encodedPassword");

        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);

        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRoleRepository.findByUser(user)).thenReturn(userRoles);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRoleRepository, times(1)).findByUser(user);

        // Verify logging
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals(Level.DEBUG, captorLoggingEvent.getAllValues().get(0).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(0).getFormattedMessage().contains("Loading user by username: testuser@example.com"));

        assertEquals(Level.INFO, captorLoggingEvent.getAllValues().get(1).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(1).getFormattedMessage().contains("User found with username: testuser@example.com"));
    }

    @Test
    void testLoadUserByUsername_UserFound_NoRoles() {
        // Arrange
        String email = "testuser@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        List<UserRole> userRoles = new ArrayList<>();

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRoleRepository.findByUser(user)).thenReturn(userRoles);

        // Act & Assert
        CustomException.DefaultRoleNotFoundException thrownException = assertThrows(CustomException.DefaultRoleNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        assertEquals("Default role ROLE_USER not found", thrownException.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRoleRepository, times(1)).findByUser(user);

        // Verify logging
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> logEvents = captorLoggingEvent.getAllValues();

        assertTrue(logEvents.stream().anyMatch(event ->
                event.getLevel().equals(Level.DEBUG) &&
                        event.getFormattedMessage().contains("Loading user by username: testuser@example.com")
        ));

        assertTrue(logEvents.stream().anyMatch(event ->
                event.getLevel().equals(Level.WARN) &&
                        event.getFormattedMessage().contains("No roles found for user with username: testuser@example.com")
        ));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "nonexistentuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        CustomException.UserNotFoundException exception = assertThrows(CustomException.UserNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);

        // Verify logging
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        assertEquals(Level.DEBUG, captorLoggingEvent.getAllValues().get(0).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(0).getFormattedMessage().contains("Loading user by username: nonexistentuser@example.com"));

        assertEquals(Level.WARN, captorLoggingEvent.getAllValues().get(1).getLevel());
        assertTrue(captorLoggingEvent.getAllValues().get(1).getFormattedMessage().contains("User not found with username: nonexistentuser@example.com"));
    }
}

