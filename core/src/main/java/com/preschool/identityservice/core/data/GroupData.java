package com.preschool.identityservice.core.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/** Data object for group information */
@Data
public class GroupData {
    private UUID groupId;
    private String id; // Keycloak ID for backward compatibility
    private String name; // Keycloak name for backward compatibility
    private String groupName;
    private String description;
    private String keycloakId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserData> users;
    private List<RoleData> roles;
}
