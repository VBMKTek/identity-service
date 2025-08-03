package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionRequest {

    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 50, message = "Permission name must be between 2 and 50 characters")
    private String permissionName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
