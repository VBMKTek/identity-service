package com.preschool.identityservice.common.exception;

import com.preschool.identityservice.common.enumeration.IdentityErrorCode;
import com.preschool.libraries.base.exception.ApplicationException;

/** Exception thrown when a user is not found */
public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException(String userId) {
        super(
                IdentityErrorCode.USER_NOT_FOUND.getHttpStatusCode(),
                IdentityErrorCode.USER_NOT_FOUND.getCode(),
                "User not found with ID: " + userId);
    }

    public UserNotFoundException(String field, String value) {
        super(
                IdentityErrorCode.USER_NOT_FOUND.getHttpStatusCode(),
                IdentityErrorCode.USER_NOT_FOUND.getCode(),
                "User not found with " + field + ": " + value);
    }
}
