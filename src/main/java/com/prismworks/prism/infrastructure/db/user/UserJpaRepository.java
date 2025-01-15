package com.prismworks.prism.infrastructure.db.user;

import com.prismworks.prism.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<Users, String> {
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
