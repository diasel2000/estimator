package com.estimator.controller;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.model.dto.RegisterRequest;
import com.estimator.services.SubscriptionService;
import com.estimator.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getGoogleId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update-subscription")
    public ResponseEntity<User> updateSubscription(@RequestParam String subscriptionName, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Subscription subscription = subscriptionService.getSubscriptionByName(subscriptionName);
        userService.updateSubscription(user, subscription);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/email/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}