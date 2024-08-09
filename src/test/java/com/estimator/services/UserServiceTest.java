package com.estimator.services;

import static org.junit.jupiter.api.Assertions.*;

import com.estimator.model.*;
import com.estimator.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    }

    @Test
    void testRegisterUser_UsernameExists() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(new User());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(username, "testuser@example.com", "password", "");
        });

        assertEquals("Username is already exist", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserByEmail_Success() {
        String email = "testuser@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        userService.deleteUserByEmail(email);

        verify(userRepository).deleteByEmail(email);
    }

    @Test
    void testDeleteUserByEmail_UserNotFound() {
        String email = "testuser@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUserByEmail(email);
        });

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, never()).deleteByEmail(email);
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
    void testRegisterUser_DefaultRoleNotFound() {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password";
        String googleId = "";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(username, email, password, googleId);
        });

        assertEquals("Default role not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userRoleRepository, never()).save(any(UserRole.class));
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

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(username, email, password, googleId);
        });

        assertEquals("Default subscription not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userRoleRepository, never()).save(any(UserRole.class));
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

        verify(subscription, times(1)).getSubscriptionName();
    }
}
