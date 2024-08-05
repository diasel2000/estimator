package com.estimator.controller;

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

@Controller
public class DashboardController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public DashboardController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String googleId = authentication.getName();

        User user = userService.findByGoogleID(googleId);
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        for (Subscription subscription:subscriptions){
            String subscriptionName =subscription.getSubscriptionName();
            if (subscriptionName.equals("Basic")){
                model.addAttribute("user", user);
                return "dashboardB";
            }
            if (subscriptionName.equals("Standard")){
                model.addAttribute("user", user);
                return "dashboardS";
            }
            if (subscriptionName.equals("Ultimate")){
                model.addAttribute("user", user);
                return "dashboardU";
            }

        }
        //TODO implement role permission check

        //TODO implement templates for difrent subscriptions and roles

        model.addAttribute("user", user);
        return "dashboard";
    }
}
