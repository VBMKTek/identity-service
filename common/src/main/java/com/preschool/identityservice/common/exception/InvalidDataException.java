package com.preschool.identityservice.common.exception;

import com.preschool.identityservice.common.enumeration.IdentityErrorCode;
import com.preschool.libraries.base.exception.ApplicationException;

/**
 * Exception thrown when invalid data is provided
 */
public class InvalidDataException extends ApplicationException {
    
    public InvalidDataException(String message) {
        super(IdentityErrorCode.INVALID_DATA.getHttpStatusCode(), 
              IdentityErrorCode.INVALID_DATA.getCode(), 
              message);
    }
    
    public InvalidDataException(IdentityErrorCode errorCode, String customMessage) {
        super(errorCode.getHttpStatusCode(), 
              errorCode.getCode(), 
              customMessage);
    }
}
