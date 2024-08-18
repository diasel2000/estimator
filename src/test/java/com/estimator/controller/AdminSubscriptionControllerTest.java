package com.estimator.controller;

import com.estimator.facade.SubscriptionFacade;
import com.estimator.model.Subscription;
import com.estimator.services.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AdminSubscriptionControllerTest {

    @Mock
    private SubscriptionFacade subscriptionFacade;

    @InjectMocks
    private AdminSubscriptionController adminSubscriptionController;

    private final ByteArrayOutputStream logOutput = new ByteArrayOutputStream();
    private static final Logger logger = LoggerFactory.getLogger(AdminSubscriptionController.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(logOutput));
    }

    @Test
    public void testGetAllSubscriptions() {
        // Arrange
        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        when(subscriptionFacade.getAllSubscriptions()).thenReturn(subscriptions);

        // Act
        ResponseEntity<List<Subscription>> response = adminSubscriptionController.getAllSubscriptions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subscriptions, response.getBody());
        verify(subscriptionFacade, times(1)).getAllSubscriptions();

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching all subscriptions."));
        assertTrue(logContent.contains("Managed subscriptions - Total subscriptions: 2"));
    }

    @Test
    public void testDeleteSubscription_Success() {
        // Arrange
        Long subscriptionId = 1L;

        when(subscriptionFacade.existsById(subscriptionId)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> response = adminSubscriptionController.deleteSubscription(subscriptionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Subscription deleted successfully", response.getBody().get("message"));
        verify(subscriptionFacade, times(1)).deleteSubscription(subscriptionId);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Deleted subscription with ID: " + subscriptionId));
    }

    @Test
    public void testDeleteSubscription_NotFound() {
        // Arrange
        Long subscriptionId = 1L;

        when(subscriptionFacade.existsById(subscriptionId)).thenReturn(false);

        // Act
        ResponseEntity<Map<String, String>> response = adminSubscriptionController.deleteSubscription(subscriptionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Subscription not found", response.getBody().get("error"));
        verify(subscriptionFacade, times(0)).deleteSubscription(subscriptionId);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Attempted to delete subscription with ID: " + subscriptionId + " - Subscription not found"));
    }
}
