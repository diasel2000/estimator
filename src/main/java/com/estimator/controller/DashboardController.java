package com.estimator.controller;

import com.estimator.model.Role;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final UserService userService;

    @Autowired
    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        logger.debug("Fetching user details for username: {}", username);

        User user = userService.findByUserName(username);
        if (user == null) {
            logger.info("User not found by username. Trying to find by Google ID: {}", username);
            user = userService.findByGoogleID(username);
        }

        if (user != null) {
            logger.info("User found. Username: {}", user.getUsername());
            model.addAttribute("user", user);

            Subscription subscription = user.getSubscription();
            String subscriptionName = subscription != null ? subscription.getSubscriptionName() : "No Subscription";
            logger.debug("User subscription: {}", subscriptionName);

            Set<Role> roles = user.getRoles();
            model.addAttribute("roles", roles);
            model.addAttribute("subscriptionName", subscriptionName);

            switch (subscriptionName) {
                case "Basic":
                    logger.debug("Redirecting to Basic dashboard.");
                    return "dashboardB";
                case "Standard":
                    logger.debug("Redirecting to Standard dashboard.");
                    return "dashboardS";
                case "Ultimate":
                    logger.debug("Redirecting to Ultimate dashboard.");
                    return "dashboardU";
                default:
                    logger.debug("Redirecting to default dashboard.");
                    return "dashboard";
            }
        }

        logger.warn("User not found. Redirecting to error page.");
        return "error";
    }
}
