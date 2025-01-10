package com.prismworks.prism.domain.post.domain.repository;

import com.prismworks.prism.domain.post.domain.model.PostTeamRecruitment;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.domain.model.TeamRecruitmentPosition;

@Repository
public interface TeamRecruitmentPositionRepository extends JpaRepository<TeamRecruitmentPosition, Long> {
	List<TeamRecruitmentPosition> findByPostTeamRecruitment(PostTeamRecruitment postTeamRecruitment);
}
