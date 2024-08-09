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

@Controller
public class DashboardController {

    private final UserService userService;

    @Autowired
    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        if (user==null) {
            user = userService.findByGoogleID(username);
        }

        if (user != null) {
            model.addAttribute("user", user);
            Subscription subscription = user.getSubscription();
            String subscriptionName = subscription != null ? subscription.getSubscriptionName() : "No Subscription";

            Set<Role> roles = user.getRoles();
            model.addAttribute("roles", roles);
            model.addAttribute("subscriptionName", subscriptionName);

            switch (subscriptionName) {
                case "Basic":
                    return "dashboardB";
                case "Standard":
                    return "dashboardS";
                case "Ultimate":
                    return "dashboardU";
                default:
                    return "dashboard";
            }
        }

        return "error";
    }
}
