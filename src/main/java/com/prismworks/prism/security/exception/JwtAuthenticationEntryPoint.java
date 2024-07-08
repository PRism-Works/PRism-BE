package com.prismworks.prism.security.exception;

import com.prismworks.prism.common.exception.ApplicationException;
import com.prismworks.prism.domain.auth.exception.AuthErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.security.SignatureException;

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
        if(e instanceof UnsupportedJwtException ||
            e instanceof MalformedJwtException ||
            e instanceof SignatureException
        ) {
            return new ApplicationException(AuthErrorCode.INVALID_TOKEN);
        }

        if(e instanceof ExpiredJwtException) {
            return new ApplicationException(AuthErrorCode.TOKEN_ALREADY_EXPIRED);
        }

        return new ApplicationException(AuthErrorCode.AUTH_FAILED);
    }
}
