package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignUserPermissionRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Role name is required")
    private String roleName;
}
