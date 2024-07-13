package com.prismworks.prism.security.exception;

import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.domain.auth.exception.AuthErrorCode;
import com.prismworks.prism.domain.auth.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.security.SignatureException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        Exception exception = (Exception) request.getAttribute("exception");
        ApplicationException applicationException = this.convertException(exception);
        resolver.resolveException(request, response, null, applicationException);
    }

    private ApplicationException convertException(Exception e) {
        log.error("filter exception: ", e);
        if(e instanceof UnsupportedJwtException ||
            e instanceof MalformedJwtException ||
            e instanceof SignatureException
        ) {
            return AuthException.INVALID_TOKEN;
        }

        if(e instanceof ExpiredJwtException) {
            return AuthException.TOKEN_ALREADY_EXPIRED;
        }

        if(e instanceof AuthException authException) {
            return authException;
        }

        return AuthException.AUTH_FAILED;
    }
}
