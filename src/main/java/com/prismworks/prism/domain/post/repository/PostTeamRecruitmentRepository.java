package com.prismworks.prism.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.model.PostTeamRecruitment;

@Repository
public interface PostTeamRecruitmentRepository extends JpaRepository<PostTeamRecruitment, Long> {}
