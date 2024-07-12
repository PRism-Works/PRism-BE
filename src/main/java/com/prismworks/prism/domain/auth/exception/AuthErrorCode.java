package com.prismworks.prism.domain.auth.exception;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import com.prismworks.prism.common.exception.ApplicationErrorCode;
import org.antlr.v4.runtime.atn.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ApplicationErrorCode {
    CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "AuthCode_400_1", "인증코드가 일치하지 않습니다."),
    CODE_ALREADY_EXPIRED(HttpStatus.BAD_REQUEST, "AuthCode_400_2", "이미 만료된 인증코드입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AuthCode_400_3", "이메일 인증을 하지 않았습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "AuthCode_401_1", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AuthCode_401_2", "유효하지 않은 토큰입니다."),
    TOKEN_ALREADY_EXPIRED(HttpStatus.UNAUTHORIZED, "AuthCode_401_3", "이미 만료된 토큰입니다."),
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, "AuthCode_401_4", "인증에 실패했습니다. 다시 인증해주세요."),
    EMAIL_NOT_REGISTERED(HttpStatus.UNAUTHORIZED, "AuthCode_401_5", "회원가입되지 않은 이메일입니다.");

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
