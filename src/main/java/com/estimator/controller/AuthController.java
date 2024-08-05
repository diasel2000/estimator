package com.estimator.controller;

import com.estimator.model.User;
import com.estimator.repository.UserRepository;
import com.estimator.services.JwtTokenProvider;
import com.estimator.services.SubscriptionService;
import com.estimator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SubscriptionService subscriptionService;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        if (userService.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        userService.registerUser(username,email,passwordEncoder.encode(password),"");
        redirectAttributes.addFlashAttribute("message", "Registration successful");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtTokenProvider.createToken(user.getEmail(), new ArrayList<>(user.getRoles())); // Fix roles cast
            session.setAttribute("token", token); // Save token in session
            // Check user subscription and redirect accordingly
            if (userHasSubscription(user)) {
                return "redirect:/dashboard"; // Or the appropriate page based on the user's subscription
            } else {
                return "redirect:/subscribe"; // Redirect to subscription page
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    private boolean userHasSubscription(User user) {
        // Check if the user has a subscription
        return user.getSubscription() != null;
    }
}
