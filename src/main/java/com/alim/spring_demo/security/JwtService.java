package com.alim.spring_demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Secret key — in production this goes in application.properties, not here
    private static final String SECRET = "your-super-secret-key-that-is-long-enough-32chars";
    private static final long EXPIRATION_MS = 86400000; // 24 hours

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate a token for a given email
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getKey())
                .compact();
    }

    // Extract the email from a token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if token is still valid
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email)
                && !extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}