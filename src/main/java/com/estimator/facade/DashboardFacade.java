package com.estimator.facade;

import com.estimator.model.User;
import com.estimator.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class DashboardFacade {

    private final UserService userService;

    public DashboardFacade(UserService userService) {
        this.userService = userService;
    }

    public User getUserDetails(String username) {
        User user = userService.findByUserName(username);
        if (user == null) {
            user = userService.findByGoogleID(username);
        }
        return user;
    }
}
