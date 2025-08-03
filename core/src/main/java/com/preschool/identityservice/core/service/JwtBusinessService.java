package com.preschool.identityservice.core.service;

import com.preschool.identityservice.core.data.JwtVerificationData;
import com.preschool.identityservice.core.data.TokenData;
import com.preschool.identityservice.core.data.UserData;
import com.preschool.identityservice.core.service.infra.JwtDataAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Business logic service for JWT operations
 * Contains all business rules and validation for JWT management
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class JwtBusinessService {

    private final JwtDataAccessService jwtDataAccessService;
    private final UserBusinessService userBusinessService;

    /**
     * Generate JWT token for authenticated user
     */
    public TokenData generateToken(String username, String password) {
        log.info("Generating JWT token for user: {}", username);
        
        // Authenticate user and get user data
        UserData userData = authenticateUser(username, password);
        
        // Generate token through data access service
        TokenData tokenData = jwtDataAccessService.generateToken(userData);
        
        log.info("JWT token generated successfully for user: {}", username);
        return tokenData;
    }

    /**
     * Verify JWT token and return user information
     */
    public JwtVerificationData verifyToken(String token) {
        log.debug("Verifying JWT token");
        
        try {
            JwtVerificationData verificationData = jwtDataAccessService.verifyToken(token);
            
            if (verificationData.isValid()) {
                log.debug("JWT token verified successfully for user: {}", verificationData.getUsername());
            } else {
                log.warn("JWT token verification failed: {}", verificationData.getError());
            }
            
            return verificationData;
        } catch (Exception e) {
            log.error("Error verifying JWT token", e);
            return JwtVerificationData.builder()
                    .valid(false)
                    .error("Token verification failed: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Refresh JWT token
     */
    public TokenData refreshToken(String refreshToken) {
        log.info("Refreshing JWT token");
        
        try {
            TokenData tokenData = jwtDataAccessService.refreshToken(refreshToken);
            log.info("JWT token refreshed successfully");
            return tokenData;
        } catch (Exception e) {
            log.error("Error refreshing JWT token", e);
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }

    /**
     * Revoke JWT token
     */
    public void revokeToken(String token) {
        log.info("Revoking JWT token");
        
        try {
            jwtDataAccessService.revokeToken(token);
            log.info("JWT token revoked successfully");
        } catch (Exception e) {
            log.error("Error revoking JWT token", e);
            throw new RuntimeException("Failed to revoke token: " + e.getMessage());
        }
    }

    /**
     * Private method to authenticate user
     */
    private UserData authenticateUser(String username, String password) {
        // This would typically involve password verification
        // For now, we'll get user by username and assume password is verified
        UserData userData = userBusinessService.getUserByUsername(username);
        
        if (userData == null) {
            throw new RuntimeException("User not found: " + username);
        }
        
        // TODO: Add password verification logic here
        // validatePassword(password, userData.getPasswordHash());
        
        return userData;
    }
}
