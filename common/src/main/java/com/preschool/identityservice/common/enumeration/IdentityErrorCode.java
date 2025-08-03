package com.preschool.identityservice.common.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enum defining all error codes used in the identity service
 * Provides centralized error code management with HTTP status mapping
 */
@Getter
@RequiredArgsConstructor
public enum IdentityErrorCode {
    
    // User related errors
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", "User already exists"),
    USER_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "USER_VALIDATION_FAILED", "User validation failed"),
    
    // Role related errors
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ROLE_NOT_FOUND", "Role not found"),
    ROLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "ROLE_ALREADY_EXISTS", "Role already exists"),
    ROLE_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "ROLE_VALIDATION_FAILED", "Role validation failed"),
    ROLE_IN_USE(HttpStatus.CONFLICT, "ROLE_IN_USE", "Role is currently in use and cannot be deleted"),
    
    // Permission related errors
    PERMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "PERMISSION_NOT_FOUND", "Permission not found"),
    PERMISSION_ALREADY_EXISTS(HttpStatus.CONFLICT, "PERMISSION_ALREADY_EXISTS", "Permission already exists"),
    PERMISSION_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "PERMISSION_VALIDATION_FAILED", "Permission validation failed"),
    PERMISSION_IN_USE(HttpStatus.CONFLICT, "PERMISSION_IN_USE", "Permission is currently in use and cannot be deleted"),
    
    // Group related errors
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_NOT_FOUND", "Group not found"),
    GROUP_ALREADY_EXISTS(HttpStatus.CONFLICT, "GROUP_ALREADY_EXISTS", "Group already exists"),
    GROUP_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "GROUP_VALIDATION_FAILED", "Group validation failed"),
    GROUP_HAS_USERS(HttpStatus.CONFLICT, "GROUP_HAS_USERS", "Group contains users and cannot be deleted"),
    
    // Authentication/Authorization errors
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", "Authentication failed"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Access denied"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "Token is invalid"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "Token has expired"),
    
    // External service errors
    KEYCLOAK_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "KEYCLOAK_SERVICE_ERROR", "Keycloak service error"),
    EXTERNAL_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "EXTERNAL_SERVICE_UNAVAILABLE", "External service unavailable"),
    
    // General validation errors
    INVALID_DATA(HttpStatus.BAD_REQUEST, "INVALID_DATA", "Invalid data provided"),
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "REQUIRED_FIELD_MISSING", "Required field is missing"),
    INVALID_UUID_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_UUID_FORMAT", "Invalid UUID format"),
    
    // Business logic errors
    OPERATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "OPERATION_NOT_ALLOWED", "Operation not allowed"),
    RESOURCE_IN_USE(HttpStatus.CONFLICT, "RESOURCE_IN_USE", "Resource is currently in use"),
    
    // System errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal server error"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "Database operation failed"),
    CONFIGURATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CONFIGURATION_ERROR", "Configuration error");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    
    /**
     * Get HTTP status code as integer
     */
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
