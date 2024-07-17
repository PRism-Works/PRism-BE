package com.prismworks.prism.domain.project.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiErrorResponse;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.project.dto.ProjectDto;
import com.prismworks.prism.domain.project.dto.ProjectResponseDto;
import com.prismworks.prism.domain.project.dto.SummaryProjectDto;
import com.prismworks.prism.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ApiSuccessResponse createProject(@RequestBody @Valid ProjectDto projectDto) throws ParseException {
        ProjectResponseDto createdProjectDto = projectService.createProject(projectDto);
        return new ApiSuccessResponse(HttpStatus.CREATED.value(), createdProjectDto);
    }

    @PutMapping("/{projectId}")
    public ApiSuccessResponse updateProject(@PathVariable int projectId, @RequestBody @Valid ProjectDto projectDto) throws ParseException {
        ProjectResponseDto updatedProjectDto = projectService.updateProject(projectId, projectDto);
        return new ApiSuccessResponse(HttpStatus.OK.value(), updatedProjectDto);
    }

    @DeleteMapping("/{projectId}")
    public ApiSuccessResponse deleteProject(@PathVariable int projectId) {
        projectService.deleteProject(projectId);
        return new ApiSuccessResponse(HttpStatus.NO_CONTENT.value(), null);
    }

    //테스트 완료
    @GetMapping("/summary/by-name")
    public ApiSuccessResponse getProjectSummaryByName(@RequestParam String projectName) {
        List<SummaryProjectDto> summaryProjects = projectService.getProjectSummaryByName(projectName);
        return new ApiSuccessResponse(HttpStatus.OK.value(), summaryProjects);
    }
    /*
    @GetMapping("/summary/by-member-and-filters")
    public ApiSuccessResponse getProjectSummaryByMemberAndFilters(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String organizationName) {
        List<SummaryProjectDto> summaryProjects = projectService.getProjectSummaryByMemberAndFilters(projectName, memberName, categories, organizationName);
        return new ApiSuccessResponse(HttpStatus.OK.value(), summaryProjects);
    }
    */
    @GetMapping("/me-involved-projects")
    public ApiSuccessResponse getMeInvolvedProjects(@CurrentUser UserContext userContext) {
        String myEmail = userContext.getEmail();
        List<SummaryProjectDto> myProjects = projectService.getMeInvolvedProjects(myEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), myProjects);
    }
    /*
    @GetMapping("/my-registered-projects")
    public ApiSuccessResponse getMyRegisteredProjects() {
        List<SummaryProjectDto> myRegisteredProjects = projectService.getMyRegisteredProjects();
        return new ApiSuccessResponse(HttpStatus.OK.value(), myRegisteredProjects);
    }
    */
}
