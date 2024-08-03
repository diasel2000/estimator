package com.estimator.controller;

import com.estimator.model.Subscription;
import com.estimator.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/subscriptions")
public class AdminSubscriptionController {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping
    public String manageSubscriptions(Model model) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        model.addAttribute("subscriptions", subscriptions);
        return "manage_subscriptions";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        subscriptionRepository.deleteById(id);
        return "redirect:/admin/subscriptions";
    }
}

