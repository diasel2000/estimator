package com.estimator.controller;

import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "dashboard";
    }

//    @GetMapping("/admin/users")
//    public String manageUsers(Model model) {
//        List<User> users = userRepository.findAll();
//        model.addAttribute("users", users);
//        return "manage_users";
//    }

//    @GetMapping("/subscriptions")
//    public String manageSubscriptions(Model model) {
//        List<Subscription> subscriptions = subscriptionRepository.findAll();
//        model.addAttribute("subscriptions", subscriptions);
//        return "manage_subscriptions";
//    }
}
