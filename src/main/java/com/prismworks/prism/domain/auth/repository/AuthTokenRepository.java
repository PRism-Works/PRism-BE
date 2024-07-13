package com.prismworks.prism.domain.auth.repository;

import com.prismworks.prism.domain.auth.model.AuthToken;
import com.prismworks.prism.domain.auth.repository.custom.AuthTokenCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer>, AuthTokenCustomRepository {
    Optional<AuthToken> findByRefreshToken(String token);
}
