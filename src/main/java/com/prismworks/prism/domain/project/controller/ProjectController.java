package com.prismworks.prism.domain.project.controller;

import com.prismworks.prism.common.response.ApiErrorResponse;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.project.dto.ProjectDto;
import com.prismworks.prism.domain.project.dto.ProjectResponseDto;
import com.prismworks.prism.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
}
