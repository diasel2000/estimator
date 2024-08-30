package com.estimator.controller;

import com.estimator.facade.DashboardFacade;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "${cors.allowed.origins}")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private static final String USER_NOT_FOUND_RETURNING_ERROR_RESPONSE = "User not found. Returning error response.";
    private static final String FETCHING_USER_DETAILS_FOR_USERNAME = "Fetching user details for username: {}";
    private static final String USER_FOUND_USERNAME = "User found. Username: {}";
    private static final String USER_SUBSCRIPTION = "User subscription: {}";
    private static final String SUBSCRIPTION_NAME = "subscriptionName";
    private static final String NO_SUBSCRIPTION = "No Subscription";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String DASHBOARD_VIEW = "dashboardView";
    private static final String ROLES = "roles";
    private static final String ERROR = "error";
    private static final String USER = "user";

    private final DashboardFacade dashboardFacade;

    @Autowired
    public DashboardController(DashboardFacade dashboardFacade) {
        this.dashboardFacade = dashboardFacade;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        String username = authentication.getName();
        logger.debug(FETCHING_USER_DETAILS_FOR_USERNAME, username);

        User user = dashboardFacade.getUserDetails(username);

        if (user == null) {
            return handleUserNotFound();
        }

        return handleUserFound(user);
    }

    private ResponseEntity<Map<String, Object>> handleUserNotFound() {
        logger.warn(USER_NOT_FOUND_RETURNING_ERROR_RESPONSE);
        Map<String, Object> response = Collections.singletonMap(ERROR, USER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private ResponseEntity<Map<String, Object>> handleUserFound(User user) {
        logger.info(USER_FOUND_USERNAME, user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put(USER, user);
        response.put(ROLES, user.getRoles());

        String subscriptionName = getSubscriptionName(user);
        response.put(SUBSCRIPTION_NAME, subscriptionName);
        response.put(DASHBOARD_VIEW, determineDashboardView(subscriptionName));

        return ResponseEntity.ok(response);
    }

    private String getSubscriptionName(User user) {
        Subscription subscription = user.getSubscription();
        String subscriptionName = subscription != null ? subscription.getSubscriptionName() : NO_SUBSCRIPTION;
        logger.debug(USER_SUBSCRIPTION, subscriptionName);
        return subscriptionName;
    }

    private String determineDashboardView(String subscriptionName) {
        return switch (subscriptionName) {
            case "Basic" -> "dashboardB";
            case "Standard" -> "dashboardS";
            case "Ultimate" -> "dashboardU";
            default -> "dashboard";
        };
    }
}

