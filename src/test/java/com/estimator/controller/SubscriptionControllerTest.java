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
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up ListAppender for capturing logs
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        listAppender = new ListAppender<>();
        listAppender.setContext(loggerContext);
        listAppender.start();
        loggerContext.getLogger(SubscriptionController.class).addAppender(listAppender);
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

        // Verify logging
        List<ILoggingEvent> logs = listAppender.list;
        assertTrue(logs.stream().anyMatch(e -> e.getFormattedMessage().contains("Fetching all subscriptions")));
        assertTrue(logs.stream().anyMatch(e -> e.getFormattedMessage().contains("Retrieved 2 subscriptions")));
    }
}
