package com.estimator.controller;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setGoogleId("google-id");

        User user = new User();
        when(userService.registerUser(anyString(), anyString(), anyString(), anyString())).thenReturn(user);

        ResponseEntity<User> response = userController.registerUser(request);

        assertEquals(ResponseEntity.ok(user), response);
        verify(userService, times(1)).registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getGoogleId());
    }

    @Test
    public void testGetProfilePage() {
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");

        User user = new User();
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        Model model = mock(Model.class);

        String viewName = userController.getProfilePage(model, authentication);

        assertEquals("profile", viewName);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testUpdateSubscription() {
        User user = new User();
        Subscription subscription = new Subscription();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(subscriptionService.getSubscriptionByName("Basic")).thenReturn(subscription);

        ResponseEntity<User> response = userController.updateSubscription("Basic", principal);

        assertEquals(ResponseEntity.ok(user), response);
        verify(userService, times(1)).updateSubscription(user, subscription);
    }

    @Test
    public void testDeleteUser() {
        ResponseEntity<Void> response = userController.deleteUser("test@example.com");

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(userService, times(1)).deleteUserByEmail("test@example.com");
    }

    @Test
    public void testGetProfilePageWithUserDetails() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");

        User user = new User();
        when(userService.findByUserName("testuser")).thenReturn(user);

        Model model = mock(Model.class);

        String viewName = userController.getProfilePage(model, authentication);

        assertEquals("profile", viewName);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testGetProfilePageUserNotFound() {
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("notfound@example.com");

        when(userService.findByEmail("notfound@example.com")).thenReturn(null);

        Model model = mock(Model.class);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getProfilePage(model, authentication);
        });

        assertEquals("User not found", thrown.getMessage());
    }

}

