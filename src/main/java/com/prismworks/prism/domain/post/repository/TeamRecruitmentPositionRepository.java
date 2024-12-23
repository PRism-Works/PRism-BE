package com.prismworks.prism.domain.post.repository;

import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRecruitmentPositionRepository extends JpaRepository<TeamRecruitmentPosition, Long> {

}
