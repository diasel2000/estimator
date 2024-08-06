package com.estimator.services;

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
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

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
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setPassword(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setGoogleID(oAuth2User.getAttribute("sub"));
            user.setUsername(oAuth2User.getAttribute("name"));
            user.setCreatedAt(LocalDateTime.now());
        }

        Optional<Role> defaultRoleOpt = Optional.ofNullable(roleRepository.findByRoleName("ROLE_ADMIN"));
        if (!defaultRoleOpt.isPresent()) {
            throw new RuntimeException("Default role not found");
        }

        Optional<Subscription> defaultSubscriptionOpt = Optional.ofNullable(subscriptionRepository.findBySubscriptionName("Basic"));
        if (!defaultSubscriptionOpt.isPresent()) {
            throw new RuntimeException("Default subscription not found");
        }

        user.setSubscription(defaultSubscriptionOpt.get());
        userRepository.save(user);

        Role defaultRole = defaultRoleOpt.get();
        assignRoleToUser(user, defaultRole);
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
    }
}

