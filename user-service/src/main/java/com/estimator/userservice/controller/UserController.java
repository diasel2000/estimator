package com.estimator.userservice.controller;
import com.estimator.dto.UserDTO;
import com.estimator.facade.AuthFacade;
import com.estimator.facade.UserFacade;
import com.estimator.model.User;
import com.estimator.services.JwtTokenProvider;
import com.estimator.userservice.dto.UserDTO;
import com.estimator.userservice.facade.AuthFacade;
import com.estimator.userservice.facade.UserFacade;
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
@CrossOrigin(origins = "${cors.allowed.origins}")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String ERROR_UPDATING_SUBSCRIPTION_FOR_USER_WITH_EMAIL = "Error updating subscription for user with email: {}";
    private static final String UPDATING_SUBSCRIPTION_FOR_USER_WITH_EMAIL = "Updating subscription for user with email: {}";
    private static final String YOU_CAN_ONLY_DELETE_YOUR_OWN_ACCOUNT = "You can only delete your own account.";
    private static final String ERROR_DELETING_USER_WITH_EMAIL = "Error deleting user with email: {}";
    private static final String DELETING_USER_WITH_EMAIL = "Deleting user with email: {}";
    private static final String SUBSCRIPTION_UPDATE_FAILED = "Subscription update failed";
    private static final String USER_DELETION_FAILED = "User deletion failed";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_ = "Bearer ";
    private static final String ERROR = "error";

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
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader(AUTHORIZATION) String authHeader) {
        if (!isValidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = extractToken(authHeader);
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getUsername(token);
        UserDTO userDTO = fetchUserDTOByEmail(email);

        return userDTO != null
                ? ResponseEntity.ok(userDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/update-subscription")
    public ResponseEntity<?> updateSubscription(@RequestParam String subscriptionName, Principal principal) {
        logger.debug(UPDATING_SUBSCRIPTION_FOR_USER_WITH_EMAIL, principal.getName());

        try {
            UserDTO updatedUserDTO = updateSubscriptionForUser(principal.getName(), subscriptionName);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (Exception e) {
            logger.error(ERROR_UPDATING_SUBSCRIPTION_FOR_USER_WITH_EMAIL, principal.getName(), e);
            return createErrorResponse(SUBSCRIPTION_UPDATE_FAILED, e);
        }
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email, Principal principal) {
        logger.debug(DELETING_USER_WITH_EMAIL, email);

        if (!isAuthorizedToDelete(email, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap(ERROR, YOU_CAN_ONLY_DELETE_YOUR_OWN_ACCOUNT));
        }

        try {
            userFacade.deleteUser(email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error(ERROR_DELETING_USER_WITH_EMAIL, email, e);
            return createErrorResponse(USER_DELETION_FAILED, e);
        }
    }

    private boolean isValidAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER_);
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    private UserDTO fetchUserDTOByEmail(String email) {
        User user = authFacade.getUserByEmail(email);
        return userFacade.userToUserDTO(user);
    }

    private UserDTO updateSubscriptionForUser(String username, String subscriptionName) throws Exception {
        User authenticatedUser = authFacade.getUserByUsername(username);
        // TODO: Add payment check logic here.
        User user = userFacade.updateSubscription(authenticatedUser.getEmail(), subscriptionName);
        return userFacade.userToUserDTO(user);
    }

    private boolean isAuthorizedToDelete(String email, Principal principal) {
        User authenticatedUser = authFacade.getUserByUsername(principal.getName());
        return authenticatedUser != null && authenticatedUser.getEmail().equals(email);
    }

    private ResponseEntity<?> createErrorResponse(String message, Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(ERROR, message + ": " + e.getMessage()));
    }
}
