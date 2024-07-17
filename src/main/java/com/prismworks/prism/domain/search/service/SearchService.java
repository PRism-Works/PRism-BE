package com.prismworks.prism.domain.search.service;

import com.prismworks.prism.common.dto.PageResponse;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.Repository.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.search.dto.ProjectSearchCondition;
import com.prismworks.prism.domain.search.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProjectProjection.ProjectSearchResult> searchProject(SearchDto.ProjectSearchRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNo(), dto.getPageSize());
        ProjectSearchCondition condition = dto.getCondition();

        Page<ProjectProjection.ProjectSearchResult> page = projectRepository.searchByCondition(condition, pageRequest);

        return new PageResponse<>(page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getContent());
    }
}
