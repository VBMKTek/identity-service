package com.preschool.identityservice.api.dto.response;

import java.time.Instant;
import java.util.List;
import lombok.Data;

/** Response object for user information */
@Data
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private Instant createdTimestamp;
    private List<RoleResponse> roles;
    private List<GroupResponse> groups;
}
