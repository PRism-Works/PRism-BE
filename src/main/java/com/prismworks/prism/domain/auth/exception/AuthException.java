package com.prismworks.prism.domain.auth.exception;

import com.prismworks.prism.common.exception.ApplicationErrorCode;
import com.prismworks.prism.common.exception.ApplicationException;

public class AuthException extends ApplicationException {

    public static final AuthException CODE_NOT_MATCH = new AuthException(AuthErrorCode.CODE_NOT_MATCH);
    public static final AuthException CODE_ALREADY_EXPIRED = new AuthException(AuthErrorCode.CODE_ALREADY_EXPIRED);
    public static final AuthException EMAIL_NOT_VERIFIED = new AuthException(AuthErrorCode.EMAIL_NOT_VERIFIED);
    public static final AuthException PASSWORD_NOT_MATCH = new AuthException(AuthErrorCode.PASSWORD_NOT_MATCH);
    public static final AuthException INVALID_TOKEN = new AuthException(AuthErrorCode.INVALID_TOKEN);
    public static final AuthException TOKEN_ALREADY_EXPIRED = new AuthException(AuthErrorCode.TOKEN_ALREADY_EXPIRED);
    public static final AuthException AUTH_FAILED = new AuthException(AuthErrorCode.AUTH_FAILED);
    public static final AuthException EMAIL_NOT_REGISTERED = new AuthException(AuthErrorCode.EMAIL_NOT_REGISTERED);

    public AuthException(String message, ApplicationErrorCode errorCode) {
        super(message, errorCode);
    }

    public AuthException(ApplicationErrorCode errorCode) {
        super(errorCode.getErrorInfo().getReason(), errorCode);
    }
}
