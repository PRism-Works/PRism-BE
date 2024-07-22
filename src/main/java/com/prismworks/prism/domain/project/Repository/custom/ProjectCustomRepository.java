package com.prismworks.prism.domain.project.Repository.custom;

import com.prismworks.prism.domain.project.Repository.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.search.dto.ProjectSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectCustomRepository {

    Page<ProjectProjection.ProjectSearchResult> searchByCondition(ProjectSearchCondition condition, Pageable pageable);

    Long countUserByProjectId(Integer projectId);
}
