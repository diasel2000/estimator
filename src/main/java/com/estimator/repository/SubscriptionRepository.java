package com.estimator.repository;


import com.estimator.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findBySubscriptionName(String name);
}
