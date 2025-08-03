package com.preschool.identityservice.common.param;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Parameter object for User operations passed between layers
 */
@Data
public class UserParam {
    
    private String username;
    private String email;
    private List<UUID> roleIds;
}
