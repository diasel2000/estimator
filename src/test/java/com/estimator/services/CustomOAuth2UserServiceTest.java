package com.estimator.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.estimator.model.User;
import com.estimator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    @Transactional
    public void testLoadUser() {
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "test@example.com");
        attributes.put("sub", "google-id");
        attributes.put("name", "Test User");

        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");
        when(oAuth2User.getAttribute("sub")).thenReturn("google-id");
        when(oAuth2User.getAttribute("name")).thenReturn("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OAuth2User result = customOAuth2UserService.loadUser(userRequest);

        verify(userRepository).save(any(User.class));
        assertNotNull(result);
        assertEquals("Test User", result.getAttribute("name"));
    }
}

