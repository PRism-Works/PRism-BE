package com.prismworks.prism.infrastructure.db.project.custom;

import com.prismworks.prism.infrastructure.db.project.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.interfaces.search.dto.ProjectSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectCustomRepository {

    Page<ProjectProjection.ProjectSearchResult> searchByCondition(ProjectSearchCondition condition, Pageable pageable);

    Long countUserByProjectId(Integer projectId);

    List<ProjectUserJoin> findAllMemberByProjectId(Integer projectId);

    Optional<ProjectUserJoin> findMemberByProjectIdAndEmail(Integer projectId, String email);
}
