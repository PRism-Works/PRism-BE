package com.prismworks.prism.domain.post.infra.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prismworks.prism.domain.post.domain.model.UserPostBookmark;

public interface UserPostBookmarkRepository extends JpaRepository<UserPostBookmark, Long> {
	Optional<UserPostBookmark> findByUserIdAndPostId(String userId, Long postId);
}
