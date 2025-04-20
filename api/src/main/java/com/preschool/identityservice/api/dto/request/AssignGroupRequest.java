package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignGroupRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Group ID is required")
    private String groupId;
}
