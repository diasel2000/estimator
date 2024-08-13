package com.estimator.controller;

import com.estimator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        logger.debug("Rendering login page.");
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        logger.debug("Rendering registration page.");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to register user with email: {}", email);

        if (userService.findByEmail(email) != null) {
            logger.warn("Registration failed: Email already exists for email: {}", email);
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/login";
        }

        userService.registerUser(username, email, password, UUID.randomUUID().toString());
        logger.info("Registration successful for user with email: {}", email);
        redirectAttributes.addFlashAttribute("message", "Registration successful");
        return "redirect:/login";
    }
}
