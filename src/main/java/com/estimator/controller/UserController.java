package com.estimator.controller;
import com.estimator.dto.UserDTO;
import com.estimator.facade.UserFacade;
import com.estimator.model.User;
import com.estimator.dto.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        logger.debug("Registering user with email: {}", request.getEmail());
        try {
            User user = userFacade.registerUser(request);
            UserDTO userDTO = userFacade.userToUserDTO(user);
            logger.info("User registered successfully with email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (Exception e) {
            logger.error("Error registering user with email: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfilePage(Authentication authentication) {
        logger.debug("Getting profile page for user");

        User user = userFacade.getCurrentUser(authentication);
        if (user == null) {
            logger.error("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User not found"));
        }

        UserDTO userDTO = userFacade.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/update-subscription")
    public ResponseEntity<?> updateSubscription(@RequestParam String subscriptionName, Principal principal) {
        logger.debug("Updating subscription for user with email: {}", principal.getName());
        try {
            User user = userFacade.updateSubscription(principal.getName(), subscriptionName);
            UserDTO userDTO = userFacade.userToUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            logger.error("Error updating subscription for user with email: {}", principal.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Subscription update failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        logger.debug("Deleting user with email: {}", email);
        try {
            userFacade.deleteUser(email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting user with email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "User deletion failed: " + e.getMessage()));
        }
    }
}