package com.preschool.identityservice.core.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Token data object for core layer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenData {
    
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Instant expiresAt;
    private Instant refreshExpiresAt;
    private UUID userId;
    private String username;
    private String email;
    private List<String> roles;
    private List<String> permissions;
}
