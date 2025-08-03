package com.preschool.identityservice.core.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Data object for role information */
@Data
public class RoleData {
    private UUID roleId;
    private String id; // Keycloak ID for backward compatibility
    private String name; // Keycloak name for backward compatibility
    private String roleName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PermissionData> permissions;
    private List<UserData> users;
}
