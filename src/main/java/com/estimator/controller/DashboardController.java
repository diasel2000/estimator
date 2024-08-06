package com.estimator.controller;

import com.estimator.model.Role;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.services.SubscriptionService;
import com.estimator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
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
        String googleId = authentication.getName();
        User user = userService.findByGoogleID(googleId);

        if (user != null) {
            model.addAttribute("user", user);
            Subscription subscription = user.getSubscription();
            String subscriptionName = subscription != null ? subscription.getSubscriptionName() : "No Subscription";

            Set<Role> roles = user.getRoles();
            model.addAttribute("roles", roles);

            if ("Basic".equals(subscriptionName)) {
                return "dashboard";
            } else if ("Standard".equals(subscriptionName)) {
                return "dashboard";
            } else if ("Ultimate".equals(subscriptionName)) {
                return "dashboard";
            }
        }

        return "dashboard";
    }
}
