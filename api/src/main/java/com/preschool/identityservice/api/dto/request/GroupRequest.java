package com.preschool.identityservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GroupRequest {

    @NotBlank(message = "Group name is required")
    @Size(min = 2, max = 50, message = "Group name must be between 2 and 50 characters")
    private String groupName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private List<UUID> userIds;
    
    private List<UUID> roleIds;
}
