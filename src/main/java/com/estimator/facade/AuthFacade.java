package com.estimator.facade;

import com.estimator.dto.RegisterRequest;
import com.estimator.model.User;
import com.estimator.services.UserService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthFacade {

    private final UserService userService;

    public AuthFacade(UserService userService) {
        this.userService = userService;
    }

    public boolean isEmailExists(String email) {
        return userService.findByEmail(email) != null;
    }

    public void registerUser(RegisterRequest request) {
        userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), UUID.randomUUID().toString());
    }

    public User authenticateUser(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && userService.isPasswordValid(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserByEmail(String email) {
        return userService.findByEmail(email);
    }
}

