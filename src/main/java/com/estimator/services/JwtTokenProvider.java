package com.estimator.services;

import com.estimator.model.Role;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class JwtTokenProvider {

    private final KeyPair keyPair;
    private final Logger logger;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public JwtTokenProvider() {
        this(LoggerFactory.getLogger(JwtTokenProvider.class));
    }

    public JwtTokenProvider(Logger logger) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            this.keyPair = keyPairGenerator.generateKeyPair();
            this.logger = logger;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }

    public String createToken(String email, List<Role> roles) {
        logger.debug("Creating JWT token for email: {}", email);
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles.stream().map(Role::getRoleName).collect(Collectors.toList()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.ES256, keyPair.getPrivate())
                .compact();

        logger.debug("JWT token created successfully for email: {}", email);
        return token;
    }

    public String getUsername(String token) {
        logger.debug("Extracting username from JWT token.");
        String username = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token).getBody().getSubject();
        logger.debug("Username extracted successfully from JWT token: {}", username);
        return username;
    }

    public boolean validateToken(String token) {
        try {
            logger.debug("Validating JWT token.");
            Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token);
            logger.debug("JWT token validated successfully.");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Expired or invalid JWT token", e);
            throw new RuntimeException("Expired or invalid JWT token", e);
        }
    }
}
