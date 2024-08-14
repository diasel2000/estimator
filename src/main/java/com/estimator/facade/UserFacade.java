package com.estimator.facade;

import com.estimator.dto.RegisterRequest;
import com.estimator.dto.UserDTO;
import com.estimator.model.User;
import com.estimator.services.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {

    private final SubscriptionFacade subscriptionFacade;
    private final UserRoleFacade userRoleFacade;
    private final UserService userService;

    public UserFacade(SubscriptionFacade subscriptionFacade, @Lazy UserRoleFacade userRoleFacade, UserService userService) {
        this.subscriptionFacade = subscriptionFacade;
        this.userRoleFacade = userRoleFacade;
        this.userService = userService;
    }

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(user.getUserID());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setGoogleID(user.getGoogleID());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setSubscription(subscriptionFacade.subscriptionToSubscriptionDTO(user.getSubscription()));
        userDTO.setUserRoles(user.getUserRoles().stream()
                .map(userRoleFacade::userRoleToUserRoleDTO)
                .collect(Collectors.toSet()));
        return userDTO;
    }

    public User userDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setUserID(userDTO.getUserID());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setGoogleID(userDTO.getGoogleID());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setSubscription(subscriptionFacade.subscriptionDTOToSubscription(userDTO.getSubscription()));
        user.setUserRoles(userDTO.getUserRoles().stream()
                .map(userRoleFacade::userRoleDTOToUserRole)
                .collect(Collectors.toSet()));
        return user;
    }

    public User registerRequestToUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setGoogleID(request.getGoogleId());

        return user;
    }

    public User registerUser(RegisterRequest request) {
        User user = registerRequestToUser(request);
        return userService.registerUser(user.getUsername(),user.getEmail(),user.getPassword(),user.getGoogleID());
    }

    public User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email);
    }

    public User updateSubscription(String email, String subscriptionName) {
        return userService.updateUserSubscription(email, subscriptionName);
    }

    public void deleteUser(String email) {
        userService.deleteUserByEmail(email);
    }

    public void deleteUserById(Long id) {
        userService.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userService.existsById(id);
    }

    public List<User> getAllUsers() {
        return userService.findAll();
    }
}
