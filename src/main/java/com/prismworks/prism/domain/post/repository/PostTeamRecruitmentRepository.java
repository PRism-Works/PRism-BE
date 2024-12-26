package com.prismworks.prism.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.search.dto.SearchDto;

@Repository
public interface PostTeamRecruitmentRepository extends JpaRepository<PostTeamRecruitment, Long> {
    Optional<PostTeamRecruitment> findByPostId(Long postId);

    @Query("SELECT new com.prismworks.prism.domain.search.dto.SearchDto.PostTeamRecruitmentSearchResponse(p2.projectId, ptr.recruitmentStatus) " +
        "FROM UserPostBookmark upb " +
        "JOIN Post p1 ON p1.postId = upb.postId " +
        "JOIN PostTeamRecruitment ptr ON ptr.postId = p1.postId " +
        "JOIN Project p2 ON p2.projectId = ptr.projectId " +
        "JOIN p2.categories pcj " +
        "JOIN pcj.category c " +
        "JOIN TeamRecruitmentPosition trp ON trp.postTeamRecruitmentId = ptr.postTeamRecruitmentId " +
        "WHERE upb.userId = :userId " +
        "AND upb.activeFlag = true " +
        "AND (:positions IS NULL OR trp.position IN :positions) " +
        "AND (:categories IS NULL OR c.name IN :categories) " +
        "AND (:skills IS NULL OR c.name IN :skills) " +
        "AND (:processMethod IS NULL OR ptr.processMethod = :processMethod) " +
        "AND (:recruitmentStatus IS NULL OR ptr.recruitmentStatus = :recruitmentStatus)")
    Page<SearchDto.PostTeamRecruitmentSearchResponse> findBookmarkedPostTeamRecruitments(
        @Param("userId") String userId,
        @Param("positions") List<String> positions,
        @Param("categories") List<String> categories,
        @Param("skills") List<String> skills,
        @Param("processMethod") ProcessMethod processMethod,
        @Param("recruitmentStatus") RecruitmentStatus recruitmentStatus,
        Pageable pageable
    );
}
