package com.prismworks.prism.domain.search.controller;

import java.util.List;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.dto.PageResponse;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.project.Repository.custom.projection.ProjectProjection;
import com.prismworks.prism.domain.search.dto.SearchDto;
import com.prismworks.prism.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchController implements SearchControllerDocs{

    private final SearchService searchService;

    @GetMapping("/projects")
    public ApiSuccessResponse searchProject(SearchDto.ProjectSearchRequest request) {
        PageResponse<ProjectProjection.ProjectSearchResult> response = searchService.searchProject(request);
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/posts/recruitment/bookmarked")
    public ApiSuccessResponse getBookmarkedPostTeamRecruitments(
        @CurrentUser UserContext user,
        @ModelAttribute SearchDto.PostTeamRecruitmentSearchRequest request,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    )
    {
        System.out.println("상태는 ? " + request.getRecruitmentStatus());
        Page<SearchDto.PostTeamRecruitmentSearchResponse> response = searchService.getBookmarkedPostTeamRecruitments(
            user.getUserId(), request, PageRequest.of(page, size));

        return ApiSuccessResponse.defaultOk(response);
    }
}

