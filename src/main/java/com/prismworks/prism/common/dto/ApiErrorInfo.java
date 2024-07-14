package com.prismworks.prism.common.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiErrorInfo {
    private final Integer status;
    private final String code;
    private String reason;

    public void setReason(String reason) {
        this.reason = reason;
    }
}
