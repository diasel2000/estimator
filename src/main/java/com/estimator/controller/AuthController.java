package com.estimator.controller;

import com.estimator.dto.JwtResponse;
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
import java.util.List;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());

        if (authFacade.isEmailExists(request.getEmail())) {
            logger.warn("Registration failed: Email already exists for email: {}", request.getEmail());
            return ResponseEntity.badRequest().body("Email already exists");
        }

        authFacade.registerUser(request);
        logger.info("Registration successful for user with email: {}", request.getEmail());
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest request) {
        logger.info("Attempting to authenticate user with email: {}", request.getEmail());

        User user = authFacade.authenticateUser(request.getEmail(), request.getPassword());

        if (user == null) {
            logger.warn("Authentication failed for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), (List<Role>) user.getRoles());
        logger.info("Authentication successful for email: {}", request.getEmail());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getUsername(token);
            User user = authFacade.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
