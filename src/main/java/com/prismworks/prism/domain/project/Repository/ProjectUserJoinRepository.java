package com.prismworks.prism.domain.project.Repository;

import java.util.List;

import com.prismworks.prism.domain.project.dto.MemberDetailDto;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectUserJoinRepository extends JpaRepository<ProjectUserJoin, Integer> {
    @Query("SELECT p FROM ProjectUserJoin p WHERE p.email = :email AND p.project.projectId = :projectId")
    ProjectUserJoin findByEmailAndProjectId(String email, Integer projectId);

    @Query("SELECT count(p) FROM ProjectUserJoin p WHERE p.project.projectId = :projectId AND p.peerReviewDone = true")
    int getSurveyParticipant(int projectId);

    @Query("SELECT puj.user.userId AS userId, " +
        "puj.user.userProfile.username AS username, " +
        "puj.user.userProfile.introduction AS introduction, " +
        "puj.roles AS roles, " +
        "puj.user.userProfile.skills AS skills, " +
        "puj.user.userProfile.interestJobs AS interestJobs, " +
        "puj.user.email AS email, " +
        "COUNT(puj.project.projectId) AS projectCount, " +
        "puj.anonyVisibility AS anonyVisibility " +
        "FROM ProjectUserJoin puj " +
        "WHERE puj.project.projectId = :projectId " +
        "GROUP BY puj.project.projectId, puj.user.userId")
    List<MemberDetailDto> findProjectMemberDetails(@Param("projectId") Integer projectId);
}
