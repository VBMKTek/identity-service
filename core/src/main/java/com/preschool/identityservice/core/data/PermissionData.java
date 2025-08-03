package com.preschool.identityservice.core.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Data object for permission information */
@Data
public class PermissionData {
    private UUID permissionId;
    private String permissionName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoleData> roles;
}
