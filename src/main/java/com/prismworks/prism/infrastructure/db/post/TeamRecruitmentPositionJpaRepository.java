package com.prismworks.prism.infrastructure.db.post;

import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRecruitmentPositionJpaRepository extends JpaRepository<TeamRecruitmentPosition, Long> {
	List<TeamRecruitmentPosition> findByPostTeamRecruitment(PostTeamRecruitment postTeamRecruitment);
}
