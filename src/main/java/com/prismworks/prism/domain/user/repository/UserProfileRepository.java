package com.prismworks.prism.domain.user.repository;

import com.prismworks.prism.domain.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
