package com.preschool.identityservice.core.data;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** JWT verification result data for core layer */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtVerificationData {

    private boolean valid;
    private String subject;
    private UUID userId;
    private String username;
    private String email;
    private List<String> roles;
    private List<String> permissions;
    private Instant issuedAt;
    private Instant expiresAt;
    private Map<String, Object> claims;
    private String error;
}
