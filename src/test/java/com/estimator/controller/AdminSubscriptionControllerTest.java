package com.estimator.controller;

import static org.junit.jupiter.api.Assertions.*;
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

public class AdminSubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AdminSubscriptionController adminSubscriptionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    }
}
