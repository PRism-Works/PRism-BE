package com.prismworks.prism.common.response;

import com.prismworks.prism.common.dto.ApiErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private final String code;
    private final String message;

    public ApiErrorResponse(ApiErrorInfo errorInfo) {
        this.code = errorInfo.getCode();
        this.message = errorInfo.getReason();
    }
}
