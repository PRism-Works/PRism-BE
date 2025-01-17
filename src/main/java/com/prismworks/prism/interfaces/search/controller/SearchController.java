package com.prismworks.prism.interfaces.search.controller;

import com.prismworks.prism.common.dto.PageResponse;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.project.Repository.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.search.service.SearchService;
import com.prismworks.prism.interfaces.search.dto.SearchDto.ProjectSearchRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchController implements SearchControllerDocs{

    private final SearchService searchService;

    @GetMapping("/projects")
    public ApiSuccessResponse searchProject(ProjectSearchRequest request) {
        PageResponse<ProjectProjection.ProjectSearchResult> response = searchService.searchProject(request);
        return ApiSuccessResponse.defaultOk(response);
    }
}

