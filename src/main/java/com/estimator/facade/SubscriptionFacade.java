package com.estimator.facade;

import com.estimator.dto.SubscriptionDTO;
import com.estimator.model.Subscription;
import com.estimator.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SubscriptionFacade {


    @Autowired
    private SubscriptionService subscriptionService;

    public SubscriptionDTO subscriptionToSubscriptionDTO(Subscription subscription) {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setSubscriptionID(subscription.getSubscriptionID());
        subscriptionDTO.setSubscriptionName(subscription.getSubscriptionName());
        subscriptionDTO.setDescription(subscription.getDescription());
        subscriptionDTO.setPrice(subscription.getPrice());
        return subscriptionDTO;
    }

    public Subscription subscriptionDTOToSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionID(subscriptionDTO.getSubscriptionID());
        subscription.setSubscriptionName(subscriptionDTO.getSubscriptionName());
        subscription.setDescription(subscriptionDTO.getDescription());
        subscription.setPrice(subscriptionDTO.getPrice());
        return subscription;
    }

    public List<Subscription> getAllSubscriptions() {
        return  subscriptionService.getAllSubscriptions();
    }

    public boolean existsById(Long id) {
        return subscriptionService.existsById(id);
    }

    public void deleteSubscription(Long id) {
        subscriptionService.deleteById(id);
    }
}
