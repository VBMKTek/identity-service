package com.preschool.identityservice.core.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Data object for user information */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private UUID userId;
    private String id; // Keycloak ID for backward compatibility
    private String username;
    private String email;
    private String keycloakId;
    private boolean enabled;
    private Instant createdTimestamp; // Keycloak timestamp for backward compatibility
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoleData> roles;
    private List<GroupData> groups;
}
