package com.estimator.controller;
import com.estimator.model.Subscription;
import com.estimator.model.User;
import com.estimator.repository.SubscriptionRepository;
import com.estimator.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UserController(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/subscriptions")
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @PostMapping("/subscriptions/{userId}")
    public User updateUserSubscription(@PathVariable Long userId, @RequestBody Subscription subscription) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setSubscription(subscription);
        return userRepository.save(user);
    }
}
