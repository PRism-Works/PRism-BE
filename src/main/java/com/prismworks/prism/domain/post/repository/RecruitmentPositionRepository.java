package com.prismworks.prism.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prismworks.prism.domain.post.model.RecruitmentPosition;

public interface RecruitmentPositionRepository extends JpaRepository<RecruitmentPosition, Long> {

	List<RecruitmentPosition> findByPostTeamRecruitmentId(Long postTeamRecruitmentId);
}
