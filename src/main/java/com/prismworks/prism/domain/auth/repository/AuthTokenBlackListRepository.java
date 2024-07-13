package com.prismworks.prism.domain.auth.repository;

import com.prismworks.prism.domain.auth.model.AuthTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenBlackListRepository extends JpaRepository<AuthTokenBlackList, Integer> {
    boolean existsByToken(String accessToken);
}
