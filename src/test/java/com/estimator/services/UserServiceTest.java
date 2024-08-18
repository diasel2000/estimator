package com.estimator.services;

import static org.junit.jupiter.api.Assertions.*;

import com.estimator.exception.CustomException;
import com.estimator.model.*;
import com.estimator.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.read.ListAppender;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure logback appender to capture logs
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        listAppender = new ListAppender<>();
        listAppender.setContext(loggerContext);
        listAppender.start();
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserService.class);
        logger.addAppender(listAppender);
    }

    @Test
    void testRegisterUser_Success() {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password";
        String googleId = "";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        Role defaultRole = new Role();
        defaultRole.setRoleName("ROLE_USER");

        Subscription defaultSubscription = new Subscription();
        defaultSubscription.setSubscriptionName("Basic");

        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(defaultRole);
        when(subscriptionRepository.findBySubscriptionName("Basic")).thenReturn(defaultSubscription);

        User registeredUser = userService.registerUser(username, email, password, googleId);

        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(email, registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertNotNull(registeredUser.getGoogleID());
        assertEquals(defaultSubscription, registeredUser.getSubscription());

        verify(userRepository).save(registeredUser);
        verify(userRoleRepository).save(any(UserRole.class));

        // Check the logs
        assertTrue(containsLog("Registering user with username: " + username));
        assertTrue(containsLog("User " + username + " successfully registered"));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(new User());

        CustomException.UserAlreadyExistsException exception = assertThrows(CustomException.UserAlreadyExistsException.class, () -> {
            userService.registerUser(username, "testuser@example.com", "password", "");
        });

        assertEquals("Username already exists: " + username, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));

        // Check the logs
        assertTrue(containsLog("Username " + username + " is already taken"));
    }

    @Test
    void testDeleteUserByEmail_Success() {
        String email = "testuser@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        userService.deleteUserByEmail(email);

        verify(userRepository).deleteByEmail(email);

        // Check the logs
        assertTrue(containsLog("User with email: " + email + " successfully deleted"));
    }

    @Test
    void testDeleteUserByEmail_UserNotFound() {
        String email = "testuser@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        CustomException.UserNotFoundException exception = assertThrows(CustomException.UserNotFoundException.class, () -> {
            userService.deleteUserByEmail(email);
        });

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, never()).deleteByEmail(email);

        // Check the logs
        assertTrue(containsLog("User not found with email: " + email));
    }

    @Test
    void testFindByEmail() {
        String email = "testuser@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findByEmail(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
    }

    @Test
    void testUpdateSubscription() {
        User user = new User();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionName("Premium");

        userService.updateSubscription(user, subscription);

        assertEquals(subscription, user.getSubscription());
        verify(userRepository).save(user);

        // Check the logs
        assertTrue(containsLog("Updating subscription for user " + user.getUsername() + " to " + subscription.getSubscriptionName()));
        assertTrue(containsLog("Subscription updated successfully for user " + user.getUsername()));
    }

    @Test
    void testFindByGoogleID() {
        String googleID = UUID.randomUUID().toString();
        User user = new User();
        user.setGoogleID(googleID);

        when(userRepository.findByGoogleID(googleID)).thenReturn(user);

        User foundUser = userService.findByGoogleID(googleID);

        assertNotNull(foundUser);
        assertEquals(googleID, foundUser.getGoogleID());
        verify(userRepository).findByGoogleID(googleID);
    }

    @Test
    void testRegisterUser_DefaultRoleNotFound() {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password";
        String googleId = "";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(null);

        CustomException.DefaultRoleNotFoundException exception = assertThrows(CustomException.DefaultRoleNotFoundException.class, () -> {
            userService.registerUser(username, email, password, googleId);
        });

        assertEquals("Default role ROLE_USER not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userRoleRepository, never()).save(any(UserRole.class));

        // Check the logs
        assertTrue(containsLog("Default role ROLE_USER not found"));
    }

    @Test
    void testRegisterUser_DefaultSubscriptionNotFound() {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password";
        String googleId = "";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        Role defaultRole = new Role();
        defaultRole.setRoleName("ROLE_USER");

        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(defaultRole);
        when(subscriptionRepository.findBySubscriptionName("Basic")).thenReturn(null);

        CustomException.DefaultSubscriptionNotFoundException exception = assertThrows(CustomException.DefaultSubscriptionNotFoundException.class, () -> {
            userService.registerUser(username, email, password, googleId);
        });

        assertEquals("Default subscription Basic not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userRoleRepository, never()).save(any(UserRole.class));

        // Check the logs
        assertTrue(containsLog("Default subscription Basic not found"));
    }

    @Test
    void testFindByGoogleID_WithSubscription() {
        String googleID = UUID.randomUUID().toString();
        Subscription subscription = mock(Subscription.class);
        User user = new User();
        user.setGoogleID(googleID);
        user.setSubscription(subscription);

        when(userRepository.findByGoogleID(googleID)).thenReturn(user);

        userService.findByGoogleID(googleID);

        verify(subscription).getSubscriptionName();
    }

    @Test
    void testFindByEmail_UserNotFoundLogsWarning() {
        String email = "nonexistentuser@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        User user = userService.findByEmail(email);

        assertNull(user);
        assertTrue(containsLog("User not found with email: " + email));
    }

    @Test
    void testFindByGoogleID_UserNotFoundLogsWarning() {
        String googleID = UUID.randomUUID().toString();

        when(userRepository.findByGoogleID(googleID)).thenReturn(null);

        User user = userService.findByGoogleID(googleID);

        assertNull(user);
        assertTrue(containsLog("User not found with Google ID: " + googleID));
    }


    @Test
    void testFindByUserName() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        User foundUser = userService.findByUserName(username);

        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testExistsById_Exists() {
        Long id = 1l;
        when(userRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(new User()));

        boolean exists = userService.existsById(id);

        assertTrue(exists);
        verify(userRepository).findById(Long.valueOf(id));
    }

    @Test
    void testExistsById_DoesNotExist() {
        Long id = 1l;
        when(userRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());

        boolean exists = userService.existsById(id);

        assertFalse(exists);
        verify(userRepository).findById(Long.valueOf(id));
    }

    @Test
    void testDeleteById() {
        Long id = 1l;

        userService.deleteById(id);

        verify(userRepository).deleteById(Long.valueOf(id));
    }

    private boolean containsLog(String message) {
        return listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains(message));
    }
}
