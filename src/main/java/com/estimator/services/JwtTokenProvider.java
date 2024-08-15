package com.estimator.services;

import com.estimator.model.Role;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JwtTokenProvider {

    private final KeyPair keyPair;
    private final Logger logger;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this(LoggerFactory.getLogger(JwtTokenProvider.class), userDetailsService);
    }

    public JwtTokenProvider(Logger logger, UserDetailsService userDetailsService) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            this.keyPair = keyPairGenerator.generateKeyPair();
            this.logger = logger;
            this.userDetailsService = userDetailsService;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }

    public String createToken(String email, List<Role> roles) {
        logger.debug("Creating token for email: {}", email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles.stream().map(Role::getRoleName).collect(Collectors.toList()))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.ES256)
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token).getBody();
        String username = claims.getSubject();

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
