package com.prismworks.prism.domain.post.infra.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.domain.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
	void incrementViewCountById(@Param("postId") Long postId);
}

