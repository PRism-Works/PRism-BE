package com.prismworks.prism.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;

@Repository
public interface TeamRecruitmentPositionRepository extends JpaRepository<TeamRecruitmentPosition, Long> {
	List<TeamRecruitmentPosition> findByPostTeamRecruitmentId(Long postTeamRecruitmentId);
}
