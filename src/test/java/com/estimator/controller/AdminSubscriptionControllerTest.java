package com.estimator.controller;

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

public class AdminSubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

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
    public void testManageSubscriptions() {
        // Arrange
        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        when(subscriptionService.getAllSubscriptions()).thenReturn(subscriptions);

        // Act
        String viewName = adminSubscriptionController.manageSubscriptions(model);

        // Assert
        assertEquals("manage_subscriptions", viewName);
        verify(model, times(1)).addAttribute("subscriptions", subscriptions);

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Fetching all subscriptions."));
        assertTrue(logContent.contains("Managed subscriptions - Total subscriptions: 2"));
    }

    @Test
    public void testDeleteSubscription_Success() {
        // Arrange
        Long subscriptionId = 1L;

        when(subscriptionService.existsById(subscriptionId)).thenReturn(true);

        // Act
        String viewName = adminSubscriptionController.deleteSubscription(subscriptionId, redirectAttributes);

        // Assert
        assertEquals("redirect:/admin/subscriptions", viewName);
        verify(subscriptionService, times(1)).deleteById(subscriptionId);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Subscription deleted successfully");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Deleted subscription with ID: " + subscriptionId));
    }

    @Test
    public void testDeleteSubscription_NotFound() {
        // Arrange
        Long subscriptionId = 1L;

        when(subscriptionService.existsById(subscriptionId)).thenReturn(false);

        // Act
        String viewName = adminSubscriptionController.deleteSubscription(subscriptionId, redirectAttributes);

        // Assert
        assertEquals("redirect:/admin/subscriptions", viewName);
        verify(subscriptionService, times(0)).deleteById(subscriptionId);
        verify(redirectAttributes, times(1)).addFlashAttribute("error", "Subscription not found");

        // Verify logs
        String logContent = logOutput.toString();
        assertTrue(logContent.contains("Attempted to delete subscription with ID: " + subscriptionId + " - Subscription not found"));
    }
}
