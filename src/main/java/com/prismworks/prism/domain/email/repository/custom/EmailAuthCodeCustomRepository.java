package com.prismworks.prism.domain.email.repository.custom;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailAuthCodeCustomRepository {
    List<EmailAuthCode> findAllNotVerifiedCode(String email, AuthType authType, LocalDateTime dateTime);

    Optional<EmailAuthCode> findByCode(String email, AuthType authType, String code);
}
