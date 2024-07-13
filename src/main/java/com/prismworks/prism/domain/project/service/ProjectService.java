package com.prismworks.prism.domain.project.service;

import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.dto.ProjectDto;
import com.prismworks.prism.domain.project.dto.ProjectResponseDto;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public ProjectResponseDto createProject(ProjectDto projectDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setCategories(projectDto.getCategories());
        project.setStartDate(LocalDateTime.parse(projectDto.getStartDate(), formatter));
        project.setEndDate(LocalDateTime.parse(projectDto.getEndDate(), formatter));
        project.setVisibility(true);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        List<ProjectUserJoin> members = projectDto.getMembers().stream().map(memberDto -> {
            ProjectUserJoin join = new ProjectUserJoin();
            join.setUser(userRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found")));
            join.setName(memberDto.getName());
            join.setEmail(memberDto.getEmail());
            join.setRoles(memberDto.getRoles());
            join.setProject(project);
            return join;
        }).collect(Collectors.toList());

        project.setMembers(members);
        projectRepository.save(project);

        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .categories(project.getCategories())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }

    @Transactional
    public ProjectResponseDto updateProject(int projectId, ProjectDto projectDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setCategories(projectDto.getCategories());
        project.setStartDate(LocalDateTime.parse(projectDto.getStartDate(), formatter));
        project.setEndDate(LocalDateTime.parse(projectDto.getEndDate(), formatter));
        project.setUpdatedAt(LocalDateTime.now());

        List<ProjectUserJoin> members = projectDto.getMembers().stream().map(memberDto -> {
            return project.getMembers().stream()
                    .filter(m -> m.getEmail().equals(memberDto.getEmail()))
                    .findFirst()
                    .map(existingMember -> {
                        existingMember.setRoles(memberDto.getRoles());
                        return existingMember;
                    })
                    .orElseGet(() -> {
                        ProjectUserJoin newMember = new ProjectUserJoin();
                        newMember.setUser(userRepository.findByEmail(memberDto.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found with email: " + memberDto.getEmail())));
                        newMember.setName(memberDto.getName());
                        newMember.setEmail(memberDto.getEmail());
                        newMember.setRoles(memberDto.getRoles());
                        newMember.setProject(project);
                        return newMember;
                    });
        }).collect(Collectors.toList());

        project.setMembers(members);
        projectRepository.save(project);

        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .categories(project.getCategories())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }

    @Transactional
    public ProjectResponseDto deleteProject(int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        ProjectResponseDto response = ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .categories(new ArrayList<>(project.getCategories()))
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();

        projectRepository.delete(project);
        return response;
    }
}
