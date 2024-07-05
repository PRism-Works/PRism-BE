package com.prismworks.prism.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiSuccessResponse {

    private final boolean success = true;
    private final int status ;
    private final Object data;

    public ApiSuccessResponse(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static ApiSuccessResponse defaultOk() {
        return new ApiSuccessResponse(HttpStatus.OK.value(), null);
    }

    public static ApiSuccessResponse defaultOk(Object data) {
        return new ApiSuccessResponse(HttpStatus.OK.value(), data);
    }
}
