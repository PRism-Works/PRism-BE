package com.prismworks.prism.domain.post.repository;

import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.repository.custom.PostTeamRecruitmentCustomRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.model.PostTeamRecruitment;

@Repository
public interface PostTeamRecruitmentRepository extends JpaRepository<PostTeamRecruitment, Long>,
    PostTeamRecruitmentCustomRepository {
    Optional<PostTeamRecruitment> findByPost(Post post);
}
