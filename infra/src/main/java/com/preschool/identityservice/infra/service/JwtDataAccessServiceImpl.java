package com.preschool.identityservice.infra.service;

import com.preschool.identityservice.core.data.JwtVerificationData;
import com.preschool.identityservice.core.data.TokenData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.JwtDataAccessService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * JWT data access service implementation Handles JWT token generation, verification and management
 */
@Service
@Slf4j
public class JwtDataAccessServiceImpl implements JwtDataAccessService {

    private final SecretKey secretKey;
    private final int accessTokenExpirationMinutes;
    private final int refreshTokenExpirationDays;
    private final String issuer;

    // In-memory token blacklist (in production, use Redis or database)
    private final Set<String> revokedTokens = new HashSet<>();

    public JwtDataAccessServiceImpl(
            @Value("${application.jwt.secret:mySecretKey12345678901234567890}") String secret,
            @Value("${application.jwt.access-token-expiration:60}") int accessTokenExpirationMinutes,
            @Value("${application.jwt.refresh-token-expiration:7}") int refreshTokenExpirationDays,
            @Value("${application.jwt.issuer:identity-service}") String issuer) {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays;
        this.issuer = issuer;
    }

    @Override
    public TokenData generateToken(UserData userData) {
        Instant now = Instant.now();
        Instant accessExpiry = now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES);
        Instant refreshExpiry = now.plus(refreshTokenExpirationDays, ChronoUnit.DAYS);

        // Build access token claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userData.getUserId().toString());
        claims.put("username", userData.getUsername());
        claims.put("email", userData.getEmail());
        // TODO: Add roles and permissions from userData
        claims.put("roles", List.of("USER")); // Placeholder
        claims.put("permissions", List.of("READ")); // Placeholder
        claims.put("token_type", "access");

        // Generate access token
        String accessToken =
                Jwts.builder()
                        .setClaims(claims)
                        .setSubject(userData.getUsername())
                        .setIssuer(issuer)
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(accessExpiry))
                        .signWith(secretKey, SignatureAlgorithm.HS256)
                        .compact();

        // Generate refresh token
        String refreshToken =
                Jwts.builder()
                        .setSubject(userData.getUsername())
                        .setIssuer(issuer)
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(refreshExpiry))
                        .claim("token_type", "refresh")
                        .claim("user_id", userData.getUserId().toString())
                        .signWith(secretKey, SignatureAlgorithm.HS256)
                        .compact();

        return TokenData.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresAt(accessExpiry)
                .refreshExpiresAt(refreshExpiry)
                .userId(userData.getUserId())
                .username(userData.getUsername())
                .email(userData.getEmail())
                .roles(List.of("USER")) // TODO: Get from userData
                .permissions(List.of("READ")) // TODO: Get from userData
                .build();
    }

    @Override
    public JwtVerificationData verifyToken(String token) {
        try {
            // Check if token is revoked
            if (isTokenRevoked(token)) {
                return JwtVerificationData.builder().valid(false).error("Token has been revoked").build();
            }

            // Parse and verify token
            Claims claims =
                    Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

            // Extract user information
            String userIdStr = claims.get("user_id", String.class);
            UUID userId = userIdStr != null ? UUID.fromString(userIdStr) : null;

            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);
            @SuppressWarnings("unchecked")
            List<String> permissions = claims.get("permissions", List.class);

            return JwtVerificationData.builder()
                    .valid(true)
                    .subject(claims.getSubject())
                    .userId(userId)
                    .username(claims.get("username", String.class))
                    .email(claims.get("email", String.class))
                    .roles(roles != null ? roles : List.of())
                    .permissions(permissions != null ? permissions : List.of())
                    .issuedAt(claims.getIssuedAt().toInstant())
                    .expiresAt(claims.getExpiration().toInstant())
                    .claims(new HashMap<>(claims))
                    .build();

        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            return JwtVerificationData.builder().valid(false).error("Token expired").build();
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            return JwtVerificationData.builder().valid(false).error("Malformed token").build();
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            return JwtVerificationData.builder().valid(false).error("Invalid token signature").build();
        } catch (Exception e) {
            log.error("Error verifying JWT token", e);
            return JwtVerificationData.builder().valid(false).error("Token verification failed").build();
        }
    }

    @Override
    public TokenData refreshToken(String refreshToken) {
        try {
            // Verify refresh token
            Claims claims =
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(refreshToken)
                            .getBody();

            // Check if it's a refresh token
            String tokenType = claims.get("token_type", String.class);
            if (!"refresh".equals(tokenType)) {
                throw new RuntimeException("Invalid token type for refresh");
            }

            // Extract user info
            String username = claims.getSubject();
            String userIdStr = claims.get("user_id", String.class);
            UUID userId = UUID.fromString(userIdStr);

            // Create minimal UserData for token generation
            UserData userData =
                    UserData.builder()
                            .userId(userId)
                            .username(username)
                            .email("") // TODO: Get from database if needed
                            .build();

            // Generate new tokens
            return generateToken(userData);

        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }

    @Override
    public void revokeToken(String token) {
        try {
            // Extract token ID or use the token itself as identifier
            Claims claims =
                    Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

            // Add to revoked tokens set
            revokedTokens.add(token);

            log.info("Token revoked for user: {}", claims.getSubject());
        } catch (Exception e) {
            log.error("Error revoking token", e);
            throw new RuntimeException("Failed to revoke token: " + e.getMessage());
        }
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
}
