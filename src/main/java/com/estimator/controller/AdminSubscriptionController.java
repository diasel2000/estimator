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

@Controller
@RequestMapping("/admin/subscriptions")
public class AdminSubscriptionController {

    private SubscriptionService subscriptionService;

    @Autowired
    public AdminSubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public String manageSubscriptions(Model model) {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        model.addAttribute("subscriptions", subscriptions);
        return "manage_subscriptions";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (subscriptionService.existsById(id)) {
            subscriptionService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Subscription deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Subscription not found");
        }
        return "redirect:/admin/subscriptions";
    }
}