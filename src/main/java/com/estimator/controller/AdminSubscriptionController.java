package com.estimator.controller;

import com.estimator.model.Subscription;
import com.estimator.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin/subscriptions")
public class AdminSubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSubscriptionController.class);

    private final SubscriptionService subscriptionService;

    @Autowired
    public AdminSubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public String manageSubscriptions(Model model) {
        logger.debug("Fetching all subscriptions.");
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        model.addAttribute("subscriptions", subscriptions);
        logger.info("Managed subscriptions - Total subscriptions: {}", subscriptions.size());
        return "manage_subscriptions";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (subscriptionService.existsById(id)) {
            subscriptionService.deleteById(id);
            logger.info("Deleted subscription with ID: {}", id);
            redirectAttributes.addFlashAttribute("message", "Subscription deleted successfully");
        } else {
            logger.warn("Attempted to delete subscription with ID: {} - Subscription not found", id);
            redirectAttributes.addFlashAttribute("error", "Subscription not found");
        }
        return "redirect:/admin/subscriptions";
    }
}