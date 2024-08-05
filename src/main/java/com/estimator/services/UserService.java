package com.estimator.services;

import com.estimator.model.Role;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.model.UserRole;
import com.estimator.repository.RoleRepository;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import com.estimator.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository,
                       RoleRepository roleRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password, String googleId) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setGoogleID(googleId);
        user.setCreatedAt(LocalDateTime.now());

        Optional<Role> defaultRoleOpt = Optional.ofNullable(roleRepository.findByRoleName("USER"));
        if (defaultRoleOpt.isPresent()) {
            Role defaultRole = defaultRoleOpt.get();
            userRepository.save(user);
            assignRoleToUser(user, defaultRole);
        } else {
            throw new RuntimeException("Default role not found");
        }

        Optional<Subscription> defaultSubscriptionOpt = Optional.ofNullable(subscriptionRepository.findBySubscriptionName("Basic"));
        if (defaultSubscriptionOpt.isPresent()) {
            Subscription defaultSubscription = defaultSubscriptionOpt.get();
            user.setSubscription(defaultSubscription);
        } else {
            throw new RuntimeException("Default subscription not found");
        }

        userRepository.save(user);
        return user;
    }


    public void assignRoleToUser(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }

    public User findByEmail(String email) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        return userOpt.orElse(null);
    }


    public void updateSubscription(User user, Subscription subscription) {
        user.setSubscription(subscription);
        userRepository.save(user);
    }

    public User findByGoogleID(String googleId) {
        Optional<User> userOpt = userRepository.findByGoogleID(googleId);
        return userOpt.orElse(null);
    }


}