package com.estimator.services;

import com.estimator.exception.CustomException;
import com.estimator.model.Subscription;
import com.estimator.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> getAllSubscriptions() {
        logger.debug("Fetching all subscriptions");
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        logger.info("Fetched {} subscriptions", subscriptions.size());
        return subscriptions;
    }

    public Subscription getSubscriptionByName(String name) throws CustomException.DefaultSubscriptionNotFoundException {
        logger.debug("Fetching subscription by name: {}", name);
        Optional<Subscription> subscriptionOpt = Optional.ofNullable(subscriptionRepository.findBySubscriptionName(name));
        if (subscriptionOpt.isPresent()) {
            logger.info("Subscription found with name: {}", name);
            return subscriptionOpt.get();
        } else {
            logger.warn("Subscription not found with name: {}", name);
            throw new CustomException.DefaultSubscriptionNotFoundException();
        }
    }

    public boolean existsById(Long id) {
        logger.debug("Checking if subscription exists by ID: {}", id);
        boolean exists = subscriptionRepository.existsById(id);
        if (exists) {
            logger.info("Subscription exists with ID: {}", id);
        } else {
            logger.warn("Subscription does not exist with ID: {}", id);
        }
        return exists;
    }

    public void deleteById(Long id) throws CustomException.DefaultSubscriptionNotFoundException {
        logger.debug("Deleting subscription by ID: {}", id);
        if (!existsById(id)) {
            throw new CustomException.DefaultSubscriptionNotFoundException();
        }

        try {
            subscriptionRepository.deleteById(id);
            logger.info("Deleted subscription with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting subscription with ID: {}", id, e);
            throw new RuntimeException("Error occurred while deleting subscription", e);
        }
    }
}
