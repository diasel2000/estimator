package com.estimator.services;

import com.estimator.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mockito.Mockito;
import org.slf4j.Logger;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private KeyPair keyPair;
    private Logger logger;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        keyPair = keyPairGenerator.generateKeyPair();

        // Mock the logger
        logger = Mockito.mock(Logger.class);
        jwtTokenProvider = new JwtTokenProvider(logger);

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
        verify(logger).debug("Creating JWT token for email: {}", email);
        verify(logger).debug("JWT token created successfully for email: {}", email);
    }

    @Test
    void testGetUsername() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());
        String token = jwtTokenProvider.createToken(email, roles);

        String username = jwtTokenProvider.getUsername(token);

        assertEquals(email, username);
        verify(logger).debug("Extracting username from JWT token.");
        verify(logger).debug("Username extracted successfully from JWT token: {}", email);
    }

    @Test
    void testValidateToken() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());
        String token = jwtTokenProvider.createToken(email, roles);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
        verify(logger).debug("Validating JWT token.");
        verify(logger).debug("JWT token validated successfully.");
    }

    @Test
    void testValidateTokenThrowsExceptionForExpiredToken() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());

        // Create an expired token
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles.stream().map(Role::getRoleName).collect(Collectors.toList()));
        Date now = new Date();
        Date validity = new Date(now.getTime() - 2000); // Token expired 2 seconds ago

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.ES256, keyPair.getPrivate())
                .compact();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(expiredToken);
        });

        assertFalse(exception.getMessage().contains("Expired or invalid JWT token"));
    }

    @Test
    void testValidateTokenThrowsExceptionForCorruptedToken() {
        String email = "test@example.com";
        List<Role> roles = Collections.singletonList(createRole());
        String validToken = jwtTokenProvider.createToken(email, roles);

        // Corrupt the token by modifying it
        String corruptedToken = validToken + "corrupt";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(corruptedToken);
        });

        assertFalse(exception.getMessage().contains("Expired or invalid JWT token"));
    }

    private Role createRole() {
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        return role;
    }
}