package com.preschool.identityservice.common.exception;

import com.preschool.identityservice.common.enumeration.KeycloakErrorCode;
import com.preschool.libraries.base.exception.ApplicationException;

public class KeycloakException extends ApplicationException {
    public KeycloakException(int httpStatus, String code, String message) {
        super(httpStatus, code, message);
    }

    public KeycloakException(KeycloakErrorCode keycloakErrorCode) {
        super(
                keycloakErrorCode.getHttpStatus().value(),
                keycloakErrorCode.getCode(),
                keycloakErrorCode.getMessage());
    }
}
