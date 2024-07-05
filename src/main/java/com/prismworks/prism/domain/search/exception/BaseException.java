package com.prismworks.prism.domain.search.exception;

import com.google.api.client.util.Preconditions;
import lombok.Getter;
import org.springframework.validation.BindingResult;

public class BaseException extends RuntimeException{

    @Getter
    ErrorCodes code;
    BindingResult bindingResult;

    public BaseException(ErrorCodes code, String message) {
        this(code, message, null);
    }
    public BaseException(ErrorCodes code, Throwable cause) {
        this(code, cause.getMessage(), cause);
    }

    public BaseException(ErrorCodes code, String message, Throwable cause) {
        super(message, cause);
        this.code = Preconditions.checkNotNull(code);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

}
