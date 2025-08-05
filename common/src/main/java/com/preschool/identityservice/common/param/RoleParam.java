package com.preschool.identityservice.common.param;

import java.util.List;
import java.util.UUID;
import lombok.Data;

/** Parameter object for Role operations passed between layers */
@Data
public class RoleParam {

    private String roleName;
    private String description;
    private List<UUID> permissionIds;
}
