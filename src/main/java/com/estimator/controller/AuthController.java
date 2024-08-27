package com.estimator.controller;

import com.estimator.dto.LoginRequest;
import com.estimator.dto.RegisterRequest;
import com.estimator.facade.AuthFacade;
import com.estimator.model.Role;
import com.estimator.model.User;
import com.estimator.services.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthFacade authFacade;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthFacade authFacade, JwtTokenProvider jwtTokenProvider) {
        this.authFacade = authFacade;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid RegisterRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());

        if (authFacade.isEmailExists(request.getEmail())) {
            logger.warn("Registration failed: Email already exists for email: {}", request.getEmail());
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email already exists"));
        }

        authFacade.registerUser(request);
        logger.info("Registration successful for user with email: {}", request.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration successful");
        response.put("redirectTo", "/api/auth/login");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody @Valid LoginRequest request) {
        logger.info("Attempting to authenticate user with email: {}", request.getEmail());

        User user = authFacade.authenticateUser(request.getEmail(), request.getPassword());

        if (user == null) {
            logger.warn("Authentication failed for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid credentials"));
        }

        List<Role> roles = new ArrayList<>(user.getRoles());
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("redirectTo", "/api/users/profile");
        logger.info("Authentication successful for email: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }
}
