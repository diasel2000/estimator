package com.estimator.subscriptionservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionTest {

    @Test
    public void testToString() {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionID(1l);
        subscription.setSubscriptionName("Basic");

        String expected = "Subscription{subscriptionID=1, subscriptionName='Basic'}";
        assertEquals(expected, subscription.toString());
    }
}