package com.estimator.services;

import com.estimator.exception.CustomException;
import com.estimator.model.*;
import com.estimator.repository.RoleRepository;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import com.estimator.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@CrossOrigin(origins = "${cors.allowed.origins}")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        DefaultOidcUser principal = (DefaultOidcUser) oauthToken.getPrincipal();

        String googleId = principal.getAttribute("sub");
        String email = principal.getAttribute("email");

        User user = userRepository.findByGoogleID(googleId);

        if (user == null) {

            logger.info("User not found in the database, creating a new user with email: {}", email);
            user = new User();
            user.setPassword(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setGoogleID(googleId);
            user.setUsername(principal.getAttribute("name"));
            user.setCreatedAt(LocalDateTime.now());

            Role defaultRole = roleRepository.findByRoleName("ROLE_USER");
            if (defaultRole == null) {
                logger.error("Default role not found");
                throw new CustomException.DefaultRoleNotFoundException();
            }

            Subscription defaultSubscription = subscriptionRepository.findBySubscriptionName("Basic");
            if (defaultSubscription == null) {
                logger.error("Default subscription not found");
                throw new CustomException.DefaultSubscriptionNotFoundException();
            }

            user.setSubscription(defaultSubscription);
            userRepository.saveAndFlush(user);
            logger.info("User {} saved to the database with default subscription: {}", email, "Basic");

            assignRoleToUser(user, defaultRole);
            logger.info("Role {} assigned to user: {}", defaultRole.getRoleName(), email);
            userRepository.saveAndFlush(user);
        }

        List<Role> roleList = userRepository.findRolesByUserID(user.getUserID());
        String token = jwtTokenProvider.createToken(user.getEmail(), new ArrayList<>(roleList));

        String redirectUri = "http://localhost:4200/login";

        String redirectUrlWithToken = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();

        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(redirectUrlWithToken);
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