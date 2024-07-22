package com.prismworks.prism.domain.project.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiErrorResponse;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.project.dto.*;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@Validated
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ApiSuccessResponse createProject(@CurrentUser UserContext userContext,
                                            @RequestBody @Valid ProjectDto projectDto) throws ParseException {
        projectDto.setCreatedBy(userContext.getEmail());
        ProjectResponseDto createdProjectDto = projectService.createProject(userContext,projectDto);
        return new ApiSuccessResponse(HttpStatus.CREATED.value(), createdProjectDto);
    }

    @PutMapping("/{projectId}")
    public ApiSuccessResponse updateProject(@CurrentUser UserContext userContext,
                                            @PathVariable int projectId,
                                            @RequestBody @Valid ProjectDto projectDto) throws ParseException {
        projectDto.setCreatedBy(userContext.getEmail());
        ProjectResponseDto updatedProjectDto = projectService.updateProject(userContext.getEmail(),projectId, projectDto);
        return new ApiSuccessResponse(HttpStatus.OK.value(), updatedProjectDto);
    }

    @DeleteMapping("/{projectId}")
    public ApiSuccessResponse deleteProject(@CurrentUser UserContext userContext,
                                            @PathVariable int projectId) {
        projectService.deleteProject(userContext.getEmail(),projectId);
        return new ApiSuccessResponse(HttpStatus.NO_CONTENT.value(), null);
    }

    //테스트 완료
    @GetMapping("/summary/by-name")
    public ApiSuccessResponse getProjectSummaryByName(@RequestParam String projectName) {
        List<SummaryProjectDto> summaryProjects = projectService.getProjectSummaryByName(projectName);
        return new ApiSuccessResponse(HttpStatus.OK.value(), summaryProjects);
    }


    @GetMapping("/summary/by-member-and-filters")
    public ApiSuccessResponse getProjectSummaryByMemberAndFilters(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String organizationName) {
        List<SummaryProjectDto> summaryProjects = projectService.getProjectSummaryByMemberAndFilters(projectName, memberName, categories, organizationName);
        return new ApiSuccessResponse(HttpStatus.OK.value(), summaryProjects);
    }

    @GetMapping("/me-involved-projects")
    public ApiSuccessResponse getMeInvolvedProjects(@CurrentUser UserContext userContext) {
        String myEmail = userContext.getEmail();
        List<SummaryProjectDto> myProjects = projectService.getMeInvolvedProjects(myEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), myProjects);
    }

    @GetMapping("/me-registered-projects")
    public ApiSuccessResponse getMyRegisteredProjects(@CurrentUser UserContext userContext) {
        String myEmail = userContext.getEmail();
        List<SummaryProjectDto> myRegisteredProjects = projectService.getMeRegisteredProjects(myEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), myRegisteredProjects);
    }

    @GetMapping("/me-involved-projects/{projectId}")
    public ApiSuccessResponse getProjectDetail(@CurrentUser UserContext userContext,
                                               @PathVariable int projectId) {
        String myEmail = userContext.getEmail();
        ProjectDetailDto projectDetail = projectService.getProjectDetailInMyPage(myEmail, projectId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectDetail);
    }

    @GetMapping("/summary/detail/{projectId}")
    public ApiSuccessResponse getProjectDetail(@PathVariable int projectId) {
        ProjectDetailDto projectDetail = projectService.getProjectDetailInRetrieve(projectId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectDetail);
    }


    @PostMapping("/link-project/{projectId}")
    public ApiSuccessResponse linkAnonymousProjectToUserAccount(@CurrentUser UserContext userContext,
                                                                @PathVariable int projectId,
                                                                @RequestParam String anonymousEmail) {
        ProjectDetailDto projectDetail = projectService.linkAnonymousProjectToUserAccount(userContext, projectId, anonymousEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectDetail);
    }

    //mypage에서 visibility 설정 하는 api
    @PutMapping("/visibility")
    public ApiSuccessResponse updateProjectVisibility(@RequestBody ProjectVisibilityUpdateDto request) {
        ProjectVisibilityUpdateDto projectVisibilityUpdateDto = projectService.updateProjectVisibility(request.getProjectId(), request.isVisibility());
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectVisibilityUpdateDto);
    }
}