package com.estimator.controller;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.model.dto.RegisterRequest;
import com.estimator.services.SubscriptionService;
import com.estimator.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest request) {
        logger.debug("Registering user with email: {}", request.getEmail());
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getGoogleId());
        logger.info("User registered successfully with email: {}", request.getEmail());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, Authentication authentication) {
        logger.debug("Getting profile page for user");

        User user = null;

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            user = userService.findByEmail(email);
        } else if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            user = userService.findByUserName(username);
        }

        if (user == null) {
            logger.error("User not found");
            throw new RuntimeException("User not found");
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update-subscription")
    public ResponseEntity<User> updateSubscription(@RequestParam String subscriptionName, Principal principal) {
        logger.debug("Updating subscription for user with email: {}", principal.getName());

        User user = userService.findByEmail(principal.getName());
        Subscription subscription = subscriptionService.getSubscriptionByName(subscriptionName);
        userService.updateSubscription(user, subscription);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/email/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        logger.debug("Deleting user with email: {}", email);

        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}