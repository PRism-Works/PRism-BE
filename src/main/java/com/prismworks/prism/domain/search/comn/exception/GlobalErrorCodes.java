package com.prismworks.prism.domain.search.comn.exception;

public enum GlobalErrorCodes implements ErrorCodes {

    DEFAULT_GLOBAL_ERROR_CODE("GB0000"),      // status 500
    UNKNOWN_SERVER_ERROR_CODE("GB0001"),      // status 500
    NOT_FOUND_CODE("GB0002"),                 // status 404
    BAD_REQUEST_CODE("GB0003"),               // status 400
    ACCESS_DENIED_CODE("GB0004"),             // status 403
    AUTH_ERROR_CODE("GB0005"),                // status 401
    INVALID_USERNAME_PASSWORD_CODE("GB0006"), // status 400
    INVALID_TOKEN_CODE("GB0007"),            // status 401
    PAN_DEPLOY_LOCKED_ERROR_CODE("GB0010"),   // status 423
    SCHEDULED_ARTICLES_EXIST_ERROR_CODE("GB0011");   // status 400

    String errorCode;

    GlobalErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

}