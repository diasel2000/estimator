package com.estimator.services;

import com.estimator.model.Role;
import com.estimator.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService, JwtTokenProvider jwtTokenProvider,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenProvider.createToken(user.getEmail(), (List<Role>) user.getRoles());
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }

    public void logout() {
        // Invalidate token or perform other logout operations
    }
}
