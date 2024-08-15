package com.estimator.controller;
import com.estimator.dto.UserDTO;
import com.estimator.facade.AuthFacade;
import com.estimator.facade.UserFacade;
import com.estimator.model.User;
import com.estimator.services.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserFacade userFacade;
    private final AuthFacade authFacade;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserFacade userFacade, AuthFacade authFacade, JwtTokenProvider jwtTokenProvider) {
        this.userFacade = userFacade;
        this.authFacade = authFacade;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getUsername(token);
                User user = authFacade.getUserByEmail(email);
                UserDTO userDTO = userFacade.userToUserDTO(user);
                if (userDTO != null) {
                    return ResponseEntity.ok(userDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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