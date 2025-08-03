package com.preschool.identityservice.common.param;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Parameter object for Group operations passed between layers
 */
@Data
public class GroupParam {
    
    private String groupName;
    private String description;
    private List<UUID> userIds;
    private List<UUID> roleIds;
}
