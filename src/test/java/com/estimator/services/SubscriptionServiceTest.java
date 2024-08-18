package com.estimator.services;

import static org.junit.jupiter.api.Assertions.*;

import com.estimator.exception.CustomException;
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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.argThat;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Logger logger = (Logger) LoggerFactory.getLogger(SubscriptionService.class);
        logger.addAppender(mockAppender);
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
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Fetching all subscriptions")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Fetched 2 subscriptions")));
    }

    @Test
    void testGetSubscriptionByName_SubscriptionFound() throws CustomException.DefaultSubscriptionNotFoundException {
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
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Fetching subscription by name: Basic")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Subscription found with name: Basic")));
    }

    @Test
    void testGetSubscriptionByName_SubscriptionNotFound() {
        // Arrange
        String subscriptionName = "Nonexistent";
        when(subscriptionRepository.findBySubscriptionName(subscriptionName)).thenReturn(null);

        // Act & Assert
        CustomException.DefaultSubscriptionNotFoundException exception = assertThrows(CustomException.DefaultSubscriptionNotFoundException.class, () -> {
            subscriptionService.getSubscriptionByName(subscriptionName);
        });

        assertEquals("Default subscription Basic not found", exception.getMessage());
        verify(subscriptionRepository, times(1)).findBySubscriptionName(subscriptionName);
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Fetching subscription by name: Nonexistent")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Subscription not found with name: Nonexistent")));
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
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Checking if subscription exists by ID: 1")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Subscription exists with ID: 1")));
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
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Checking if subscription exists by ID: 1")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Subscription does not exist with ID: 1")));
    }

    @Test
    void testDeleteById() throws CustomException.DefaultSubscriptionNotFoundException {
        // Arrange
        Long id = 1L;

        when(subscriptionRepository.existsById(id)).thenReturn(true);
        doNothing().when(subscriptionRepository).deleteById(id);

        // Act
        subscriptionService.deleteById(id);

        // Assert
        verify(subscriptionRepository, times(1)).deleteById(id);
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Deleting subscription by ID: 1")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Deleted subscription with ID: 1")));
    }

    @Test
    void testDeleteById_SubscriptionNotFound() {
        // Arrange
        Long id = 1L;

        when(subscriptionRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        CustomException.DefaultSubscriptionNotFoundException thrownException = assertThrows(CustomException.DefaultSubscriptionNotFoundException.class, () -> {
            subscriptionService.deleteById(id);
        });

        assertEquals("Default subscription Basic not found", thrownException.getMessage());
        verify(subscriptionRepository, times(1)).existsById(id);
        verify(subscriptionRepository, never()).deleteById(id);
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Checking if subscription exists by ID: 1")));
        verify(mockAppender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Subscription does not exist with ID: 1")));
    }
}
