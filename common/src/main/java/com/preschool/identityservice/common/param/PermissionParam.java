package com.preschool.identityservice.common.param;

import lombok.Data;

/**
 * Parameter object for Permission operations passed between layers
 */
@Data
public class PermissionParam {
    
    private String permissionName;
    private String description;
}
