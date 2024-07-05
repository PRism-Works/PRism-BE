package com.prismworks.prism.domain.email.exception;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import com.prismworks.prism.common.exception.ApplicationErrorCode;
import org.springframework.http.HttpStatus;

public enum EmailErrorCode implements ApplicationErrorCode {
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Email_500_1", "이메일 발송에 실패했습니다."),
    EMAIL_MESSAGE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Email_500_2", "이메일 메세지 생성에 실패했습니다");

    private final HttpStatus status;
    private final String code;
    private final String reason;

    EmailErrorCode(HttpStatus status, String code, String reason) {
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
