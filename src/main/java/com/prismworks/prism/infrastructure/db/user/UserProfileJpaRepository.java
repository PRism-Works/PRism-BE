package com.prismworks.prism.infrastructure.db.user;

import com.prismworks.prism.domain.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileJpaRepository extends JpaRepository<UserProfile, String> {
}
