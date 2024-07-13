package com.prismworks.prism.domain.project.service;

import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.dto.ProjectDto;
import com.prismworks.prism.domain.project.dto.ProjectResponseDto;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public ProjectResponseDto createProject(ProjectDto projectDto) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setOrganizationName(projectDto.getOrganizationName());
        project.setMemberCount(projectDto.getMemberCount());
        project.setCategories(projectDto.getCategories());
        project.setHashTags(projectDto.getHashTags());
        project.setSkills(projectDto.getSkills());
        project.setStartDate(sdf.parse(projectDto.getStartDate()));
        project.setEndDate(sdf.parse(projectDto.getEndDate()));
        project.setProjectUrlLink(projectDto.getProjectUrlLink());
        project.setVisibility(true);
        project.setCreatedAt(new Date());
        project.setUpdatedAt(new Date());

        List<ProjectUserJoin> members = projectDto.getMembers().stream().map(memberDto -> {
            ProjectUserJoin join = new ProjectUserJoin();
            join.setUser(userRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found")));
            join.setName(memberDto.getName());
            join.setEmail(memberDto.getEmail());
            join.setRoles(memberDto.getRoles());
            join.setSkills(memberDto.getSkills());
            join.setProject(project);
            return join;
        }).collect(Collectors.toList());

        project.setMembers(members);
        projectRepository.save(project);

        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .organizationName(project.getOrganizationName())
                .memberCount(project.getMemberCount())
                .categories(project.getCategories())
                .hashTags(project.getHashTags())
                .skills(project.getSkills())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .projectUrlLink(project.getProjectUrlLink())
                .build();
    }

    @Transactional
    public ProjectResponseDto updateProject(int projectId, ProjectDto projectDto) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setOrganizationName(projectDto.getOrganizationName());
        project.setMemberCount(projectDto.getMemberCount());
        project.setCategories(projectDto.getCategories());
        project.setHashTags(projectDto.getHashTags());
        project.setSkills(projectDto.getSkills());
        project.setStartDate(sdf.parse(projectDto.getStartDate()));
        project.setEndDate(sdf.parse(projectDto.getEndDate()));
        project.setProjectUrlLink(projectDto.getProjectUrlLink());
        project.setUpdatedAt(new Date());

        List<ProjectUserJoin> members = projectDto.getMembers().stream().map(memberDto -> {
            return project.getMembers().stream()
                    .filter(m -> m.getEmail().equals(memberDto.getEmail()))
                    .findFirst()
                    .map(existingMember -> {
                        existingMember.setRoles(memberDto.getRoles());
                        existingMember.setSkills(memberDto.getSkills());
                        return existingMember;
                    })
                    .orElseGet(() -> {
                        ProjectUserJoin newMember = new ProjectUserJoin();
                        newMember.setUser(userRepository.findByEmail(memberDto.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found with email: " + memberDto.getEmail())));
                        newMember.setName(memberDto.getName());
                        newMember.setEmail(memberDto.getEmail());
                        newMember.setRoles(memberDto.getRoles());
                        newMember.setSkills(memberDto.getSkills());
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
                .organizationName(project.getOrganizationName())
                .memberCount(project.getMemberCount())
                .categories(project.getCategories())
                .hashTags(project.getHashTags())
                .skills(project.getSkills())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .projectUrlLink(project.getProjectUrlLink())
                .build();
    }

    @Transactional
    public ProjectResponseDto deleteProject(int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        Hibernate.initialize(project.getCategories());
        Hibernate.initialize(project.getHashTags());
        Hibernate.initialize(project.getSkills());
        Hibernate.initialize(project.getMembers());

        ProjectResponseDto response = ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .organizationName(project.getOrganizationName())
                .memberCount(project.getMemberCount())
                .categories(project.getCategories())
                .hashTags(project.getHashTags())
                .skills(project.getSkills())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .projectUrlLink(project.getProjectUrlLink())
                .build();

        projectRepository.delete(project);
        return response;
    }
}
