package com.preschool.identityservice.core.data;

import lombok.Data;

/** Data object for operation status */
@Data
public class OperationData {
    private boolean success;
    private String message;
    private String userId;
    private String groupId;
    private String roleName;
}
