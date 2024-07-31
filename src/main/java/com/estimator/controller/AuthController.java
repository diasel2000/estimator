package com.estimator.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("name", principal.getAttribute("name"));
        return "dashboard";
    }
}