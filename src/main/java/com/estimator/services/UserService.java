package com.estimator.services;

import com.estimator.exception.CustomException;
import com.estimator.model.*;
import com.estimator.repository.RoleRepository;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import com.estimator.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    @Transactional
    public User registerUser(String username, String email, String password, String googleId) {
        logger.info("Registering user with username: {} and email: {}", username, email);

        if (userRepository.findByUsername(username) != null) {
            logger.warn("Username {} is already taken", username);
            throw new CustomException.UserAlreadyExistsException(username);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        if (googleId.isEmpty()) {
            googleId = UUID.randomUUID().toString();
            logger.debug("Generated new Google ID: {}", googleId);
        }
        user.setGoogleID(googleId);
        user.setCreatedAt(LocalDateTime.now());

        Role defaultRole = roleRepository.findByRoleName("ROLE_USER");
        if (defaultRole == null) {
            logger.error("Default role ROLE_USER not found");
            throw new CustomException.DefaultRoleNotFoundException();
        }

        Subscription defaultSubscription = subscriptionRepository.findBySubscriptionName("Basic");
        if (defaultSubscription == null) {
            logger.error("Default subscription Basic not found");
            throw new CustomException.DefaultSubscriptionNotFoundException();
        }

        user.setSubscription(defaultSubscription);
        userRepository.save(user);
        logger.info("User {} successfully registered", username);

        assignRoleToUser(user, defaultRole);

        return user;
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        logger.info("Attempting to delete user with email: {}", email);

        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.deleteByEmail(email);
            logger.info("User with email: {} successfully deleted", email);
        } else {
            logger.warn("User not found with email: {}", email);
            throw new CustomException.UserNotFoundException(email);
        }
    }

    private void assignRoleToUser(User user, Role role) {
        logger.debug("Assigning role {} to user {}", role.getRoleName(), user.getUsername());

        UserRoleKey key = new UserRoleKey();
        key.setUserID(user.getUserID());
        key.setRoleID(role.getRoleID());

        UserRole userRole = new UserRole();
        userRole.setId(key);
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        logger.info("Role {} successfully assigned to user {}", role.getRoleName(), user.getUsername());
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            logger.info("User found with email: {}", email);
        } else {
            logger.warn("User not found with email: {}", email);
        }
        return user;
    }

    public void updateSubscription(User user, Subscription subscription) {
        logger.info("Updating subscription for user {} to {}", user.getUsername(), subscription.getSubscriptionName());
        user.setSubscription(subscription);
        userRepository.save(user);
        logger.info("Subscription updated successfully for user {}", user.getUsername());
    }

    @Transactional(readOnly = true)
    public User findByGoogleID(String googleID) {
        logger.debug("Finding user by Google ID: {}", googleID);
        User user = userRepository.findByGoogleID(googleID);
        if (user != null) {
            logger.info("User found with Google ID: {}", googleID);
            user.getRoles().size(); // Trigger lazy loading if needed
            if (user.getSubscription() != null) {
                user.getSubscription().getSubscriptionName(); // Trigger lazy loading if needed
            }
        } else {
            logger.warn("User not found with Google ID: {}", googleID);
        }
        return user;
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User updateUserSubscription(String email, String subscriptionName) {
        User user = findByEmail(email);
        Subscription subscription = subscriptionRepository.findBySubscriptionName(subscriptionName);
        if (subscription != null) {
            user.setSubscription(subscription);
        } else {
            throw new CustomException.DefaultSubscriptionNotFoundException();
        }
        return userRepository.save(user);
    }
}