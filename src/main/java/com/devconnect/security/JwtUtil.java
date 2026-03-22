package com.devconnect.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtil — handles everything about JWT tokens:
 * 1. Generating a token when user logs in
 * 2. Reading the email from a token
 * 3. Checking if a token is still valid
 */
@Component
public class JwtUtil {

    // These values come from application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    // Convert the secret string into a cryptographic key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Create a JWT token for a user after they log in.
     * The token contains the user's email and expires after jwtExpiration ms.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)                                         // store email inside token
                .setIssuedAt(new Date())                                   // when token was created
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // when it expires
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)       // sign it so it can't be faked
                .compact();
    }

    /**
     * Extract the email from a JWT token.
     * Called every request to know which user is making the request.
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Check if a token is valid:
     * - Signature must match (not tampered with)
     * - Must not be expired
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid or expired
            return false;
        }
    }
}