package com.prismworks.prism.infrastructure.db.post;

import com.prismworks.prism.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
	int incrementViewCountById(@Param("postId") Long postId);
}

