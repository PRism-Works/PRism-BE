package com.prismworks.prism.common.exception;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ApplicationErrorCode{
    ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "Global_400_1", "Argument not valid"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Global_404_1", "Resource not found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Global_500_1", "internal server error");

    private final HttpStatus status;
    private final String code;
    private final String reason;

    GlobalErrorCode(HttpStatus status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }


    @Override
    public ApiErrorInfo getErrorInfo() {
        return ApiErrorInfo.builder()
                .status(this.status.value())
                .code(this.code)
                .reason(this.reason)
                .build();
    }
}
