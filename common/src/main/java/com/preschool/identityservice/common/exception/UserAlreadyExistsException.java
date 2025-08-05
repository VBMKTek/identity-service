package com.preschool.identityservice.common.exception;

import com.preschool.identityservice.common.enumeration.IdentityErrorCode;
import com.preschool.libraries.base.exception.ApplicationException;

/** Exception thrown when trying to create a user that already exists */
public class UserAlreadyExistsException extends ApplicationException {

    public UserAlreadyExistsException(String field, String value) {
        super(
                IdentityErrorCode.USER_ALREADY_EXISTS.getHttpStatusCode(),
                IdentityErrorCode.USER_ALREADY_EXISTS.getCode(),
                "User already exists with " + field + ": " + value);
    }
}
