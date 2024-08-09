package com.estimator.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.estimator.model.Subscription;
import com.estimator.services.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSubscriptions() {
        // Arrange
        Subscription subscription1 = new Subscription("Basic");

        Subscription subscription2 = new Subscription("Premium");

        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        when(subscriptionService.getAllSubscriptions()).thenReturn(subscriptions);

        // Act
        ResponseEntity<List<Subscription>> response = subscriptionController.getAllSubscriptions();

        // Assert
        assertEquals(ResponseEntity.ok(subscriptions), response);
        verify(subscriptionService, times(1)).getAllSubscriptions();
    }
}
