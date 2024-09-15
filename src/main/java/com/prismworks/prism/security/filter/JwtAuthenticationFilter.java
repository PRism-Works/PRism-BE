package com.prismworks.prism.security.filter;

import com.prismworks.prism.domain.auth.exception.AuthException;
import com.prismworks.prism.domain.auth.provider.JwtTokenProvider;
import com.prismworks.prism.domain.auth.service.AuthTokenBlackListService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer";

    private final JwtTokenProvider tokenProvider;
    private final AuthTokenBlackListService authTokenBlackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.extractTokenFromHeader(request);
        try {
            this.checkTokenInBlackList(token);
            tokenProvider.validateToken(token);
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private void checkTokenInBlackList(String token) {
        boolean isInBlackList = authTokenBlackListService.existsByToken(token);
        if(isInBlackList) {
            throw AuthException.INVALID_TOKEN;
        }
    }
}
