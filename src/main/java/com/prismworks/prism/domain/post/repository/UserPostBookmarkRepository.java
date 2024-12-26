package com.prismworks.prism.domain.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prismworks.prism.domain.post.model.UserPostBookmark;

public interface UserPostBookmarkRepository extends JpaRepository<UserPostBookmark, Long> {
	Optional<UserPostBookmark> findByUserIdAndPostId(String userId, Long postId);
}
