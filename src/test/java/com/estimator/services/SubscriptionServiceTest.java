package com.estimator.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.estimator.model.Subscription;
import com.estimator.repository.SubscriptionRepository;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSubscriptions() {
        // Arrange
        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        // Act
        List<Subscription> result = subscriptionService.getAllSubscriptions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    void testGetSubscriptionByName_SubscriptionFound() {
        // Arrange
        String subscriptionName = "Basic";
        Subscription subscription = new Subscription();
        subscription.setSubscriptionName(subscriptionName);

        when(subscriptionRepository.findBySubscriptionName(subscriptionName)).thenReturn(subscription);

        // Act
        Subscription result = subscriptionService.getSubscriptionByName(subscriptionName);

        // Assert
        assertNotNull(result);
        assertEquals(subscriptionName, result.getSubscriptionName());
        verify(subscriptionRepository, times(1)).findBySubscriptionName(subscriptionName);
    }

    @Test
    void testGetSubscriptionByName_SubscriptionNotFound() {
        // Arrange
        String subscriptionName = "Nonexistent";
        when(subscriptionRepository.findBySubscriptionName(subscriptionName)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.getSubscriptionByName(subscriptionName);
        });

        assertEquals("Subscription not found", exception.getMessage());
        verify(subscriptionRepository, times(1)).findBySubscriptionName(subscriptionName);
    }

    @Test
    void testExistsById_Exists() {
        // Arrange
        Long id = 1L;
        when(subscriptionRepository.existsById(id)).thenReturn(true);

        // Act
        boolean result = subscriptionService.existsById(id);

        // Assert
        assertTrue(result);
        verify(subscriptionRepository, times(1)).existsById(id);
    }

    @Test
    void testExistsById_DoesNotExist() {
        // Arrange
        Long id = 1L;
        when(subscriptionRepository.existsById(id)).thenReturn(false);

        // Act
        boolean result = subscriptionService.existsById(id);

        // Assert
        assertFalse(result);
        verify(subscriptionRepository, times(1)).existsById(id);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Long id = 1L;

        doNothing().when(subscriptionRepository).deleteById(id);

        // Act
        subscriptionService.deleteById(id);

        // Assert
        verify(subscriptionRepository, times(1)).deleteById(id);
    }
}
