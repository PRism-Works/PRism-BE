package com.prismworks.prism.domain.email.repository.custom;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthCodeCustomRepository {
    Optional<EmailAuthCode> findValidCode(String email, AuthType authType, LocalDateTime dateTime);
}
