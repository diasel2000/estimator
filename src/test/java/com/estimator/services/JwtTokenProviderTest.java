package com.estimator.services;

import com.estimator.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private KeyPair keyPair;
    private Logger logger;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        keyPair = keyPairGenerator.generateKeyPair();

        // Mock the logger and UserDetailsService
        logger = Mockito.mock(Logger.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);

        jwtTokenProvider = new JwtTokenProvider(logger, userDetailsService);

        // Use reflection to set the private keyPair field
        Field keyPairField = JwtTokenProvider.class.getDeclaredField("keyPair");
        keyPairField.setAccessible(true);
        keyPairField.set(jwtTokenProvider, keyPair);

        // Set validity to 1 second for testing
        Field validityField = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityField.setAccessible(true);
        validityField.set(jwtTokenProvider, 1000); // 1 second validity
    }

    @Test
    void testCreateToken() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());

        String token = jwtTokenProvider.createToken(email, roles);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(logger).debug("Creating token for email: {}", email);
    }

    @Test
    void testGetUsername() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());
        String token = jwtTokenProvider.createToken(email, roles);

        String username = jwtTokenProvider.getUsername(token);

        assertEquals(email, username);

        InOrder inOrder = inOrder(logger);
        inOrder.verify(logger).debug("Creating token for email: {}", email);
    }

    @Test
    void testValidateToken() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());
        String token = jwtTokenProvider.createToken(email, roles);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
        verify(logger).debug("Creating token for email: {}", email);
    }

    @Test
    void testGetAuthentication() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());
        String token = jwtTokenProvider.createToken(email, roles);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(authentication.getPrincipal(), userDetails);
        verify(logger).debug("Creating token for email: {}", email);
    }

    @Test
    void testResolveToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");

        String token = jwtTokenProvider.resolveToken(request);

        assertEquals("validtoken", token);
    }

    @Test
    void testResolveTokenWithoutBearerPrefix() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Invalidtoken");

        String token = jwtTokenProvider.resolveToken(request);

        assertNull(token);
    }

    private Role createRole() {
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        return role;
    }
}