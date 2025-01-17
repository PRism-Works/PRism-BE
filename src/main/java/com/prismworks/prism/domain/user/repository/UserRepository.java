package com.prismworks.prism.domain.user.repository;

import com.prismworks.prism.domain.user.model.Users;
import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<Users> getUserById(String userId);
    Optional<Users> getUserByEmail(String email);
    Users saveUser(Users user);
}
