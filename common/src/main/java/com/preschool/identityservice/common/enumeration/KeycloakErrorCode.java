package com.preschool.identityservice.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/** Error codes for Keycloak operations */
@Getter
@AllArgsConstructor
public enum KeycloakErrorCode {
    USER_EXISTS("USER_EXISTS", "Username or email already exists", HttpStatus.CONFLICT),
    GROUP_EXISTS("GROUP_EXISTS", "Group name already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND),
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", "Group not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "Role not found", HttpStatus.NOT_FOUND),
    CANNOT_ASSIGN_ROLE_TO_USER(
            "CANNOT_ASSIGN_ROLE_TO_USER", "Cannot assign role to user", HttpStatus.BAD_REQUEST),
    CANNOT_ASSIGN_ROLE_TO_GROUP(
            "CANNOT_ASSIGN_ROLE_TO_GROUP", "Cannot assign role to group", HttpStatus.BAD_REQUEST),
    CANNOT_ADD_USER_TO_GROUP(
            "CANNOT_ADD_USER_TO_GROUP", "Cannot add user to group", HttpStatus.BAD_REQUEST),
    CANNOT_REMOVE_USER_FROM_GROUP(
            "CANNOT_REMOVE_USER_FROM_GROUP", "Cannot remove user from group", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
