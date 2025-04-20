package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignGroupPermissionRequest {
    @NotBlank(message = "Group ID is required")
    private String groupId;

    @NotBlank(message = "Role name is required")
    private String roleName;
}
