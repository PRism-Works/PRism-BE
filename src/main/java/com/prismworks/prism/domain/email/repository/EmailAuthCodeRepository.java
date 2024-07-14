package com.prismworks.prism.domain.email.repository;

import com.prismworks.prism.domain.email.model.AuthType;
import com.prismworks.prism.domain.email.model.EmailAuthCode;
import com.prismworks.prism.domain.email.repository.custom.EmailAuthCodeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthCodeRepository extends JpaRepository<EmailAuthCode, Integer>, EmailAuthCodeCustomRepository {
    Optional<EmailAuthCode> findByEmailAndAuthType(String email, AuthType authType);

    Optional<EmailAuthCode> findByEmailAndCodeAndAuthType(String email, String code, AuthType authType);

    boolean existsByEmailAndVerifiedAtIsNotNull(String email);
}
