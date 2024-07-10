package com.prismworks.prism.domain.email.exception;

import com.prismworks.prism.common.exception.ApplicationErrorCode;
import com.prismworks.prism.common.exception.ApplicationException;

public class EmailException extends ApplicationException {

    public static final EmailException EMAIL_SEND_FAILED = new EmailException(EmailErrorCode.EMAIL_SEND_FAILED);
    public static final EmailException EMAIL_MESSAGE_CREATION_FAILED = new EmailException(EmailErrorCode.EMAIL_MESSAGE_CREATION_FAILED);

    public EmailException(String message, ApplicationErrorCode errorCode) {
        super(message, errorCode);
    }

    public EmailException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
