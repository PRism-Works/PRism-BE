package com.prismworks.prism.infrastructure.db.user.impl;

import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserRepository;
import com.prismworks.prism.infrastructure.db.user.UserJpaRepository;
import com.prismworks.prism.infrastructure.db.user.UserProfileJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserProfileJpaRepository userProfileJpaRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<Users> getUserById(String userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public Users saveUser(Users user) {
        return userJpaRepository.save(user);
    }
}
