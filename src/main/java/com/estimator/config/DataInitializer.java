package com.estimator.config;

import com.estimator.model.*;
import com.estimator.repository.RoleRepository;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import com.estimator.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Running DataInitializer...");

        if (userRepository.findByUsername("admin")==null) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@example.com");
            user.setGoogleID("adminGoogleID");
            user.setCreatedAt(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode("admin"));

            Optional<Role> defaultRoleOpt = Optional.ofNullable(roleRepository.findByRoleName("ROLE_ADMIN"));
            if (defaultRoleOpt.isEmpty()) {
                throw new RuntimeException("Default role not found");
            }

            Optional<Subscription> defaultSubscriptionOpt = Optional.ofNullable(subscriptionRepository.findBySubscriptionName("Ultimate"));
            if (defaultSubscriptionOpt.isEmpty()) {
                throw new RuntimeException("Default subscription not found");
            }

            user.setSubscription(defaultSubscriptionOpt.get());
            userRepository.save(user);

            Role defaultRole = defaultRoleOpt.get();
            assignRoleToUser(user, defaultRole);

            logger.info("Admin user created with Ultimate subscription.");
        } else {
            logger.info("Admin user already exists.");
        }
    }

    private void assignRoleToUser(User user, Role role) {
        UserRoleKey key = new UserRoleKey();
        key.setUserID(user.getUserID());
        key.setRoleID(role.getRoleID());

        UserRole userRole = new UserRole();
        userRole.setId(key);
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }
}
