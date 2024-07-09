package com.prismworks.prism.domain.auth.exception;

import com.prismworks.prism.common.exception.ApplicationErrorCode;
import com.prismworks.prism.common.exception.ApplicationException;

public class AuthException extends ApplicationException {
    private ApplicationErrorCode errorCode;

    public AuthException(String message, ApplicationErrorCode errorCode) {
        super(message, errorCode);
    }

    public AuthException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
