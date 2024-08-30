package com.estimator.controller;

import com.estimator.model.Subscription;
import com.estimator.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(origins = "${cors.allowed.origins}")
public class SubscriptionController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    public static final String FETCHING_ALL_SUBSCRIPTIONS = "Fetching all subscriptions";
    public static final String RETRIEVED_SUBSCRIPTIONS = "Retrieved {} subscriptions";

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        logger.debug(FETCHING_ALL_SUBSCRIPTIONS);
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        logger.info(RETRIEVED_SUBSCRIPTIONS, subscriptions.size());
        return ResponseEntity.ok(subscriptions);
    }
}