package com.estimator.controller;

import com.estimator.facade.SubscriptionFacade;
import com.estimator.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/subscriptions")
@CrossOrigin(origins = "${cors.allowed.origins}")
public class AdminSubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSubscriptionController.class);

    private static final String FETCHING_ALL_SUBSCRIPTIONS = "Fetching all subscriptions.";
    private static final String MANAGED_SUBSCRIPTIONS_TOTAL_SUBSCRIPTIONS = "Managed subscriptions - Total subscriptions: {}";
    private static final String ATTEMPTED_TO_DELETE_SUBSCRIPTION_WITH_ID_SUBSCRIPTION_NOT_FOUND = "Attempted to delete subscription with ID: {} - Subscription not found";
    private static final String DELETED_SUBSCRIPTION_WITH_ID = "Deleted subscription with ID: {}";
    private static final String SUBSCRIPTION_DELETED_SUCCESSFULLY = "Subscription deleted successfully";
    private static final String SUBSCRIPTION_NOT_FOUND = "Subscription not found";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    private final SubscriptionFacade subscriptionFacade;

    @Autowired
    public AdminSubscriptionController(SubscriptionFacade subscriptionFacade) {
        this.subscriptionFacade = subscriptionFacade;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        logger.debug(FETCHING_ALL_SUBSCRIPTIONS);
        List<Subscription> subscriptions = subscriptionFacade.getAllSubscriptions();
        logger.info(MANAGED_SUBSCRIPTIONS_TOTAL_SUBSCRIPTIONS, subscriptions.size());
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubscription(@PathVariable Long id) {
        if (!subscriptionFacade.existsById(id)) {
            return handleSubscriptionNotFound(id);
        }

        subscriptionFacade.deleteSubscription(id);
        return handleSubscriptionDeletionSuccess(id);
    }

    private ResponseEntity<Map<String, String>> handleSubscriptionNotFound(Long id) {
        logger.warn(ATTEMPTED_TO_DELETE_SUBSCRIPTION_WITH_ID_SUBSCRIPTION_NOT_FOUND, id);
        Map<String, String> response = Collections.singletonMap(ERROR, SUBSCRIPTION_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private ResponseEntity<Map<String, String>> handleSubscriptionDeletionSuccess(Long id) {
        logger.info(DELETED_SUBSCRIPTION_WITH_ID, id);
        Map<String, String> response = Collections.singletonMap(MESSAGE, SUBSCRIPTION_DELETED_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}