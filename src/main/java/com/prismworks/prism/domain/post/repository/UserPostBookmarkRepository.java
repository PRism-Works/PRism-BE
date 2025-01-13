package com.prismworks.prism.domain.post.repository;

import com.prismworks.prism.domain.post.model.UserPostBookmark;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostBookmarkRepository extends JpaRepository<UserPostBookmark, Long> {
	Optional<UserPostBookmark> findByUserIdAndPostId(String userId, Long postId);
}
