package com.estimator.userservice.controller;


import com.estimator.userservice.dto.LoginRequest;
import com.estimator.userservice.dto.RegisterRequest;
import com.estimator.userservice.exception.CustomException;
import com.estimator.userservice.facade.AuthFacade;
import com.estimator.userservice.model.User;
import com.estimator.userservice.services.JwtTokenProvider;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${cors.allowed.origins}")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String REGISTRATION_FAILED_EMAIL_ALREADY_EXISTS_FOR_EMAIL = "Registration failed: Email already exists for email: {}";
    private static final String REGISTRATION_SUCCESSFUL_FOR_USER_WITH_EMAIL = "Registration successful for user with email: {}";
    private static final String ATTEMPTING_TO_AUTHENTICATE_USER_WITH_EMAIL = "Attempting to authenticate user with email: {}";
    private static final String REGISTRATION_FAILED_USER_ALREADY_EXISTS = "Registration failed: User already exists {}";
    private static final String ATTEMPTING_TO_REGISTER_USER_WITH_EMAIL = "Attempting to register user with email: {}";
    private static final String AUTHENTICATION_SUCCESSFUL_FOR_EMAIL = "Authentication successful for email: {}";
    private static final String AUTHENTICATION_FAILED_FOR_EMAIL = "Authentication failed for email: {}";
    private static final String REGISTRATION_SUCCESSFUL = "Registration successful";
    private static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private static final String INVALID_CREDENTIALS = "Invalid credentials";
    private static final String API_USERS_PROFILE = "/api/users/profile";
    private static final String API_AUTH_LOGIN = "/api/auth/login";
    private static final String REDIRECT_TO = "redirectTo";
    private static final String MESSAGE = "message";
    private static final String TOKEN = "token";

    private final AuthFacade authFacade;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthFacade authFacade, JwtTokenProvider jwtTokenProvider) {
        this.authFacade = authFacade;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid RegisterRequest request) {
        logger.info(ATTEMPTING_TO_REGISTER_USER_WITH_EMAIL, request.getEmail());

        if (isEmailAlreadyExists(request.getEmail())) {
            return createErrorResponse(EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        try {
            authFacade.registerUser(request);
            logger.info(REGISTRATION_SUCCESSFUL_FOR_USER_WITH_EMAIL, request.getEmail());
            return createSuccessResponse();
        } catch (CustomException.UserAlreadyExistsException e) {
            logger.error(REGISTRATION_FAILED_USER_ALREADY_EXISTS, e.getMessage());
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody @Valid LoginRequest request) {
        logger.info(ATTEMPTING_TO_AUTHENTICATE_USER_WITH_EMAIL, request.getEmail());

        User user = authFacade.authenticateUser(request.getEmail(), request.getPassword());

        if (user == null) {
            logger.warn(AUTHENTICATION_FAILED_FOR_EMAIL, request.getEmail());
            return createErrorResponse(INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), new ArrayList<>(user.getRoles()));
        logger.info(AUTHENTICATION_SUCCESSFUL_FOR_EMAIL, request.getEmail());

        return createSuccessResponseWithToken(token);
    }

    private boolean isEmailAlreadyExists(String email) {
        boolean exists = authFacade.isEmailExists(email);
        if (exists) {
            logger.warn(REGISTRATION_FAILED_EMAIL_ALREADY_EXISTS_FOR_EMAIL, email);
        }
        return exists;
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(Collections.singletonMap(MESSAGE, message));
    }

    private ResponseEntity<Map<String, String>> createSuccessResponse() {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, AuthController.REGISTRATION_SUCCESSFUL);
        response.put(REDIRECT_TO, API_AUTH_LOGIN);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, String>> createSuccessResponseWithToken(String token) {
        Map<String, String> response = new HashMap<>();
        response.put(TOKEN, token);
        response.put(REDIRECT_TO, API_USERS_PROFILE);
        return ResponseEntity.ok(response);
    }
}
