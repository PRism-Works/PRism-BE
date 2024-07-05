package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.prismworks.prism.domain.email.repository.EmailAuthCodeRepository;
import com.prismworks.prism.domain.email.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailAuthCodeService {

    private final EmailAuthCodeRepository emailAuthCodeRepository;

    private final static int DEFAULT_MIN_OF_EXPIRED_AT = 5;
    private final static int DEFAULT_RANDOM_CODE_LENGTH = 6;

    @Transactional
    public EmailAuthCode createEmailAuthCode(String email, AuthType authType, LocalDateTime requestAt) {
        this.findByEmailAndAuthType(email, authType)
                .ifPresent(emailAuthCode -> {
                    if(emailAuthCode.isValid(requestAt)) {
                        emailAuthCode.expired(requestAt);
                    }
                });

        String code = RandomStringGenerator.generate(DEFAULT_RANDOM_CODE_LENGTH);

        EmailAuthCode emailAuthCode = EmailAuthCode.builder()
                .email(email)
                .code(code)
                .authType(authType)
                .expiredAt(LocalDateTime.now().plusMinutes(DEFAULT_MIN_OF_EXPIRED_AT))
                .build();

        return emailAuthCodeRepository.save(emailAuthCode);
    }

    @Transactional(readOnly = true)
    public Optional<EmailAuthCode> findByEmailAndAuthType(String email, AuthType authType) {
        return emailAuthCodeRepository.findByEmailAndAuthType(email, authType);
    }

    @Transactional(readOnly = true)
    public Optional<EmailAuthCode> findByEmailAndCodeAndAuthType(String email, String code, AuthType authType) {
        return emailAuthCodeRepository.findByEmailAndCodeAndAuthType(email, code, authType);
    }

    @Transactional(readOnly = true)
    public boolean isEmailVerified(String email) {
        return emailAuthCodeRepository.existsByEmailAndVerifiedAtIsNotNull(email);
    }
}
