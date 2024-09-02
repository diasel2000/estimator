package com.estimator.services;

import com.estimator.exception.CustomException;
import com.estimator.model.*;
import com.estimator.repository.RoleRepository;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import com.estimator.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository, SubscriptionRepository subscriptionRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        logger.debug("Loading user from OAuth2 provider");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        logger.debug("Retrieved email from OAuth2 provider: {}", email);

        if (email == null) {
            logger.error("Email not found from OAuth2 provider");
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.info("User not found in the database, creating a new user with email: {}", email);
            user = new User();
            user.setPassword(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setGoogleID(oAuth2User.getAttribute("sub"));
            user.setUsername(oAuth2User.getAttribute("name"));
            user.setCreatedAt(LocalDateTime.now());
        } else {
            logger.info("User found in the database with email: {}", email);
        }

        // Find the default role
        Role defaultRole = roleRepository.findByRoleName("ROLE_USER");
        if (defaultRole == null) {
            logger.error("Default role not found");
            throw new CustomException.DefaultRoleNotFoundException();
        }

        // Find the default subscription
        Subscription defaultSubscription = subscriptionRepository.findBySubscriptionName("Basic");
        if (defaultSubscription == null) {
            logger.error("Default subscription not found");
            throw new CustomException.DefaultSubscriptionNotFoundException();
        }

        user.setSubscription(defaultSubscription);
        userRepository.save(user);
        logger.info("User {} saved to the database with default subscription: {}", email, "Basic");

        assignRoleToUser(user, defaultRole);
        logger.info("Role {} assigned to user: {}", defaultRole.getRoleName(), email);

        userRepository.save(user);
        return oAuth2User;
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

        logger.debug("Assigned role {} to user with ID: {}", role.getRoleName(), user.getUserID());
    }
}

