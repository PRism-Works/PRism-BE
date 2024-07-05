package com.prismworks.prism.common.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiErrorInfo {
    private final Integer status;
    private final String code;
    private final String reason;
}
