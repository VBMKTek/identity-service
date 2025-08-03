package com.preschool.identityservice.core.service;

import com.preschool.identityservice.api.dto.response.TokenResponse;
import com.preschool.identityservice.api.dto.response.TokenValidationResponse;
import com.preschool.identityservice.core.data.UserData;

/**
 * JWT Token management service interface
 */
public interface JwtTokenService {
    
    /**
     * Generate JWT token for authenticated user
     * @param userData the authenticated user data
     * @return TokenResponse containing access and refresh tokens
     */
    TokenResponse generateToken(UserData userData);
    
    /**
     * Validate JWT token
     * @param token the JWT token to validate
     * @return TokenValidationResponse with validation result and user info
     */
    TokenValidationResponse validateToken(String token);
    
    /**
     * Refresh JWT token using refresh token
     * @param refreshToken the refresh token
     * @return new TokenResponse
     */
    TokenResponse refreshToken(String refreshToken);
    
    /**
     * Get public key for token verification
     * @return public key in JWK format
     */
    String getPublicKey();
    
    /**
     * Invalidate/blacklist a token
     * @param token the token to invalidate
     */
    void invalidateToken(String token);
}
