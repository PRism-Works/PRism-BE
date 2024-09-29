package com.prismworks.prism.domain.project.Repository;

import java.util.List;
import java.util.Map;

import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectUserJoinRepository extends JpaRepository<ProjectUserJoin, Integer> {
    @Query("SELECT p FROM ProjectUserJoin p WHERE p.email = :email AND p.project.projectId = :projectId")
    ProjectUserJoin findByEmailAndProjectId(String email, Integer projectId);

    @Query("SELECT count(p) FROM ProjectUserJoin p WHERE p.project.projectId = :projectId AND p.peerReviewDone = true")
    int getSurveyParticipant(int projectId);

    @Query("SELECT p.project.projectId, COUNT(p) FROM ProjectUserJoin p " +
        "WHERE p.project.projectId IN :projectIds AND p.peerReviewDone = true " +
        "GROUP BY p.project.projectId")
    Map<Integer, Integer> findSurveyParticipantsByProjectIds(List<Integer> projectIds);
}
