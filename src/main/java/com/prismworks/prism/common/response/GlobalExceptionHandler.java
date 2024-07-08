package com.prismworks.prism.common.response;

import com.prismworks.prism.common.dto.ApiErrorInfo;
        import com.prismworks.prism.common.exception.ApplicationErrorCode;
        import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.domain.auth.exception.AuthException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseCodeException(ApplicationException e) {
        log.error("ApplicationException: {}", e.getErrorCode().getErrorInfo().getReason());
        ApplicationErrorCode errorCode = e.getErrorCode();
        ApiErrorInfo errorInfo = errorCode.getErrorInfo();

        return ResponseEntity.status(errorInfo.getStatus())
                .body(new ApiErrorResponse(errorInfo));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.error("Exception", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse("Global_500_1", "internal server error"));
    }
}
