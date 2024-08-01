package com.estimator.controller;

import com.estimator.model.User;
import com.estimator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }
}
