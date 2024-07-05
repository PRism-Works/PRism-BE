package com.prismworks.prism.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{
    private final ApplicationErrorCode errorCode;

    public ApplicationException(String message, ApplicationErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(ApplicationErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
