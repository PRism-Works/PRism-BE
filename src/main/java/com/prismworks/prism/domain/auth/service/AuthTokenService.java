package com.prismworks.prism.domain.auth.service;

import com.prismworks.prism.domain.auth.model.AuthToken;
import com.prismworks.prism.domain.auth.repository.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;

    @Transactional
    public void createAuthToken(String userId, LocalDateTime expiredAt, String token) {
        AuthToken authToken = AuthToken.builder()
                .refreshToken(token)
                .userId(userId)
                .expiredAt(expiredAt)
                .build();

        authTokenRepository.save(authToken);
    }

    @Transactional(readOnly = true)
    public Optional<AuthToken> findByToken(String token) {
        return authTokenRepository.findByRefreshToken(token);
    }

    @Transactional
    public void deleteToken(AuthToken authToken) {
        authTokenRepository.delete(authToken);
    }

    @Transactional
    public void deleteTokenByUserId(String userId) {
        authTokenRepository.deleteAllByUserId(userId);
    }
}
