package com.estimator.controller;

import com.estimator.facade.SubscriptionFacade;
import com.estimator.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/subscriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminSubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSubscriptionController.class);

    private final SubscriptionFacade subscriptionFacade;

    @Autowired
    public AdminSubscriptionController(SubscriptionFacade subscriptionFacade) {
        this.subscriptionFacade = subscriptionFacade;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        logger.debug("Fetching all subscriptions.");
        List<Subscription> subscriptions = subscriptionFacade.getAllSubscriptions();
        logger.info("Managed subscriptions - Total subscriptions: {}", subscriptions.size());
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubscription(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        if (subscriptionFacade.existsById(id)) {
            subscriptionFacade.deleteSubscription(id);
            logger.info("Deleted subscription with ID: {}", id);
            response.put("message", "Subscription deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Attempted to delete subscription with ID: {} - Subscription not found", id);
            response.put("error", "Subscription not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}