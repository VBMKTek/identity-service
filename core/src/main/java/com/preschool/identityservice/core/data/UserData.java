package com.preschool.identityservice.core.data;

import java.time.Instant;
import java.util.List;
import lombok.Data;

/** Data object for user information */
@Data
public class UserData {
    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private Instant createdTimestamp;
    private List<RoleData> roles;
    private List<GroupData> groups;
}
