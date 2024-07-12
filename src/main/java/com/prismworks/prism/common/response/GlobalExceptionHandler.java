package com.prismworks.prism.common.response;

import com.prismworks.prism.common.dto.ApiErrorInfo;
        import com.prismworks.prism.common.exception.ApplicationErrorCode;
        import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.common.exception.GlobalErrorCode;
import jakarta.persistence.EntityNotFoundException;
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getMessage());
        ApiErrorInfo errorInfo = GlobalErrorCode.ENTITY_NOT_FOUND.getErrorInfo();

        return ResponseEntity.status(errorInfo.getStatus())
                .body(new ApiErrorResponse(errorInfo));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.error("Exception", e);
        ApiErrorInfo errorInfo = GlobalErrorCode.INTERNAL_SERVER_ERROR.getErrorInfo();

        return ResponseEntity.status(errorInfo.getStatus())
                .body(new ApiErrorResponse(errorInfo));
    }
}
