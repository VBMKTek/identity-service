package com.preschool.identityservice.api.dto.response;

import lombok.Data;

/** Response object for operation status */
@Data
public class OperationResponse {
    private boolean success;
    private String message;
    private String userId;
    private String groupId;
    private String roleName;
}
