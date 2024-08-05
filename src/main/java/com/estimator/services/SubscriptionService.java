package com.estimator.services;

import com.estimator.model.Subscription;
import com.estimator.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionByName(String name) {
        Optional<Subscription> subscriptionOpt = Optional.ofNullable(subscriptionRepository.findBySubscriptionName(name));
        if (subscriptionOpt.isPresent()) {
            return subscriptionOpt.get();
        } else {
            throw new RuntimeException("Subscription not found");
        }
    }

    public boolean existsById(Long id) {
        return subscriptionRepository.existsById(id);
    }

    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }
}
