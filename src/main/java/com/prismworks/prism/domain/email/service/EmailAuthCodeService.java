package com.prismworks.prism.domain.email.service;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.prismworks.prism.domain.email.repository.EmailAuthCodeRepository;
import com.prismworks.prism.domain.email.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailAuthCodeService {

    private final EmailAuthCodeRepository emailAuthCodeRepository;

    private final static int DEFAULT_MIN_OF_EXPIRED_AT = 5;
    private final static int DEFAULT_RANDOM_CODE_LENGTH = 6;

    /**
     * todo
     * findAllUsableCode로 변경하여 해당 유형, 이메일에 대한 모든 사용가능한 코드들은 사용하지 못하도록 처리
     * */
    @Transactional
    public EmailAuthCode createEmailAuthCode(String email, AuthType authType, LocalDateTime requestAt) {
        this.findAllNotVerifiedCode(email, authType, requestAt) // 기존에 발급 받은 코드들중 사용가능한 코드 전부 만료처리
                .forEach(emailAuthCode -> emailAuthCode.expired(requestAt));

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
    public List<EmailAuthCode> findAllNotVerifiedCode(String email, AuthType authType, LocalDateTime dateTime) {
        return emailAuthCodeRepository.findAllNotVerifiedCode(email, authType, dateTime);
    }

    @Transactional(readOnly = true)
    public Optional<EmailAuthCode> findByCode(String email, String code, AuthType authType) {
        return emailAuthCodeRepository.findByCode(email, authType, code);
    }

    @Transactional(readOnly = true)
    public boolean isEmailVerified(String email, String code, AuthType authType) {
        Optional<EmailAuthCode> emailAuthCodeOptional = this.findByCode(email, code, authType);
        if(emailAuthCodeOptional.isEmpty()) {
            return false;
        }

        EmailAuthCode authCode = emailAuthCodeOptional.get();
        return authCode.isVerified();
    }
}
