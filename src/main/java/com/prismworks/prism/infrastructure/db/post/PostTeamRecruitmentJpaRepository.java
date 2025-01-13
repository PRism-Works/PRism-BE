package com.prismworks.prism.infrastructure.db.post;

import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTeamRecruitmentJpaRepository extends JpaRepository<PostTeamRecruitment, Long> {

    @Query("""
        SELECT ptr
        FROM PostTeamRecruitment ptr
        JOIN FETCH ptr.post p
        WHERE p.postId = :postId
    """)
    Optional<PostTeamRecruitment> findByPostId(Long postId);
}
