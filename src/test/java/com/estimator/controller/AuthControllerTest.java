package com.estimator.controller;


import com.estimator.dto.LoginRequest;
import com.estimator.dto.RegisterRequest;
import com.estimator.facade.AuthFacade;
import com.estimator.model.Role;
import com.estimator.model.User;
import com.estimator.model.UserRole;
import com.estimator.services.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class AuthControllerTest {

    @Mock
    private AuthFacade authFacade;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    private final ByteArrayOutputStream logOutput = new ByteArrayOutputStream();
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(logOutput));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        when(authFacade.isEmailExists(request.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> response = authController.registerUser(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody().get("message"));
        verify(authFacade, never()).registerUser(any(RegisterRequest.class));

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Registration failed: Email already exists for email: " + request.getEmail()));
    }

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        when(authFacade.isEmailExists(request.getEmail())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, String>> response = authController.registerUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful", response.getBody().get("message"));
        assertEquals("/api/auth/login", response.getBody().get("redirectTo"));
        verify(authFacade, times(1)).registerUser(request);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Registration successful for user with email: " + request.getEmail()));
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();

        when(authFacade.authenticateUser(request.getEmail(), request.getPassword())).thenReturn(null);

        // Act
        ResponseEntity<Map<String, String>> response = authController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().get("message"));

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Authentication failed for email: " + request.getEmail()));
    }
    @Test
    public void testLoginUser_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        // Create a User with email and roles
        User user = new User();
        user.setEmail(request.getEmail());
        UserRole userRole = new UserRole();

        Role role = new Role();
        role.setRoleName("ROLE_USER");

        userRole.setRole(role);
        user.setUserRoles(Set.of(userRole));

        // Mock the behavior of the AuthFacade and JwtTokenProvider
        when(authFacade.authenticateUser(request.getEmail(), request.getPassword())).thenReturn(user);
        when(jwtTokenProvider.createToken(user.getEmail(), new ArrayList<>(user.getRoles()))).thenReturn("mocked-token");

        // Act
        ResponseEntity<Map<String, String>> response = authController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token", response.getBody().get("token"));
        assertEquals("/api/users/profile", response.getBody().get("redirectTo"));

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Authentication successful for email: " + request.getEmail()));
    }
}
