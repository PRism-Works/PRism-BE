package com.prismworks.prism.domain.auth.repository.custom;

public interface AuthTokenCustomRepository {
    void deleteAllByUserId(String userId);
}
