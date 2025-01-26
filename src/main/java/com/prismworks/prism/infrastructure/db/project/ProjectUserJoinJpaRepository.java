package com.prismworks.prism.infrastructure.db.project;

import java.util.List;

import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectUserJoinJpaRepository extends JpaRepository<ProjectUserJoin, Integer> {
    @Query("SELECT p FROM ProjectUserJoin p WHERE p.email = :email AND p.project.projectId = :projectId")
    ProjectUserJoin findByEmailAndProjectId(String email, Integer projectId);

    @Query("SELECT count(p) FROM ProjectUserJoin p WHERE p.project.projectId = :projectId AND p.peerReviewDone = true")
    int getSurveyParticipant(int projectId);

    @Query("SELECT p FROM ProjectUserJoin p WHERE p.project.projectId = :projectId")
    List<ProjectUserJoin>  findByProjectId(@Param("projectId") Integer projectId);

    @Query("SELECT count(p) FROM ProjectUserJoin p WHERE p.user.userId = :userId")
    Integer countByUserId(@Param("userId") String userId);
}
