package com.prismworks.prism.domain.auth.service;

import com.prismworks.prism.domain.auth.model.AuthTokenBlackList;
import com.prismworks.prism.domain.auth.repository.AuthTokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthTokenBlackListService {

    private final AuthTokenBlackListRepository authTokenBlackListRepository;

    @Transactional
    public void createBlackList(String userId, LocalDateTime expiredAt, String accessToken) {
        AuthTokenBlackList tokenBlackList = AuthTokenBlackList.builder()
                .userId(userId)
                .token(accessToken)
                .expiredAt(expiredAt)
                .build();

        authTokenBlackListRepository.save(tokenBlackList);
    }

    @Transactional(readOnly = true)
    public boolean existsByToken(String accessToken) {
        return authTokenBlackListRepository.existsByToken(accessToken);
    }
}
