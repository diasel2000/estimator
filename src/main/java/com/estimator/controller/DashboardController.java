package com.estimator.controller;

import com.estimator.facade.DashboardFacade;
import com.estimator.model.Role;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final DashboardFacade dashboardFacade;

    @Autowired
    public DashboardController(DashboardFacade dashboardFacade) {
        this.dashboardFacade = dashboardFacade;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        String username = authentication.getName();
        logger.debug("Fetching user details for username: {}", username);

        Map<String, Object> response = new HashMap<>();
        User user = dashboardFacade.getUserDetails(username);

        if (user != null) {
            logger.info("User found. Username: {}", user.getUsername());
            Subscription subscription = user.getSubscription();
            String subscriptionName = subscription != null ? subscription.getSubscriptionName() : "No Subscription";
            logger.debug("User subscription: {}", subscriptionName);

            Set<Role> roles = user.getRoles();
            response.put("user", user);
            response.put("roles", roles);
            response.put("subscriptionName", subscriptionName);

            switch (subscriptionName) {
                case "Basic":
                    response.put("dashboardView", "dashboardB");
                    break;
                case "Standard":
                    response.put("dashboardView", "dashboardS");
                    break;
                case "Ultimate":
                    response.put("dashboardView", "dashboardU");
                    break;
                default:
                    response.put("dashboardView", "dashboard");
                    break;
            }
            return ResponseEntity.ok(response);
        }

        logger.warn("User not found. Returning error response.");
        response.put("error", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
