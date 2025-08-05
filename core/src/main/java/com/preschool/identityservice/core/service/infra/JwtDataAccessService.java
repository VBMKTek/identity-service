package com.preschool.identityservice.core.service.infra;

import com.preschool.identityservice.core.data.JwtVerificationData;
import com.preschool.identityservice.core.data.TokenData;
import com.preschool.identityservice.core.data.UserData;

/**
 * Interface for JWT data access operations This provides abstraction for JWT management following
 * clean architecture
 */
public interface JwtDataAccessService {

    /** Generate JWT token for user */
    TokenData generateToken(UserData userData);

    /** Verify JWT token and extract claims */
    JwtVerificationData verifyToken(String token);

    /** Refresh JWT token using refresh token */
    TokenData refreshToken(String refreshToken);

    /** Revoke JWT token (add to blacklist) */
    void revokeToken(String token);

    /** Check if token is revoked */
    boolean isTokenRevoked(String token);
}
