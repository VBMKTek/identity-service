package com.preschool.identityservice.core.service.infra;

/**
 * Interface for logout operations
 */
public interface LogoutService {
    
    /**
     * Revokes all tokens for a user
     * @param userId the user ID (from JWT token)
     * @return true if logout successful, false otherwise
     */
    boolean logout(String userId);
    
    /**
     * Revokes specific token
     * @param refreshToken the refresh token to revoke
     * @return true if revocation successful, false otherwise
     */
    boolean revokeToken(String refreshToken);
    
    /**
     * Revokes all sessions for a user
     * @param userId the user ID
     * @return true if all sessions revoked successfully, false otherwise
     */
    boolean revokeAllSessions(String userId);
}
