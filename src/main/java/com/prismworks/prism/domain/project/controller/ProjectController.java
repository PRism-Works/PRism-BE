package com.prismworks.prism.domain.project.controller;

import com.prismworks.prism.common.response.ApiErrorResponse;
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
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDto projectDto) {
        try {
            ProjectResponseDto createdProjectDto = projectService.createProject(projectDto);
            return new ResponseEntity<>(createdProjectDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiErrorResponse("PROJECT_CREATION_FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable int projectId, @RequestBody @Valid ProjectDto projectDto) {
        try {
            ProjectResponseDto updatedProjectDto = projectService.updateProject(projectId, projectDto);
            return ResponseEntity.ok(updatedProjectDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiErrorResponse("PROJECT_UPDATE_FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable int projectId) {
        try {
            ProjectResponseDto deletedProjectDto = projectService.deleteProject(projectId);
            return ResponseEntity.ok(deletedProjectDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiErrorResponse("PROJECT_DELETION_FAILED", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
