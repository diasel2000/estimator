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

@Component
public class JwtTokenProvider {

    private final KeyPair keyPair;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public JwtTokenProvider() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }

    public String createToken(String email, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles.stream().map(Role::getRoleName).collect(Collectors.toList()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.ES256, keyPair.getPrivate())
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token", e);
        }
    }
}
