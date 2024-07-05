package com.prismworks.prism.domain.auth.exception;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import com.prismworks.prism.common.exception.ApplicationErrorCode;
import org.antlr.v4.runtime.atn.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ApplicationErrorCode {
    CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "AuthCode_400_1", "인증코드가 일치하지 않습니다."),
    CODE_ALREADY_EXPIRED(HttpStatus.BAD_REQUEST, "AuthCode_400_2", "이미 만료된 인증코드입니다."),

    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AuthCode_400_3", "이메일 인증을 하지 않았습니다.");

    private final HttpStatus status;
    private final String code;
    private final String reason;

    AuthErrorCode(HttpStatus status, String code, String reason) {
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
