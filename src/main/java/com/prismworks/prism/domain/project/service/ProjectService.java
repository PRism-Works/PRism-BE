package com.prismworks.prism.domain.project.service;

import com.prismworks.prism.domain.project.Repository.CategoryRepository;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.dto.MemberDto;
import com.prismworks.prism.domain.project.dto.ProjectDto;
import com.prismworks.prism.domain.project.dto.ProjectResponseDto;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.domain.project.model.Category;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    private Set<Category> resolveCategories(List<String> categoryNames) {
        return categoryNames.stream()
                .map(name -> categoryRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + name)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public ProjectResponseDto createProject(ProjectDto projectDto) throws ParseException {

        if (projectDto.getProjectName() == null || projectDto.getProjectName().isEmpty()) {
            throw ProjectException.NO_PROJECT_NAME;
        }

        if (projectDto.getMembers() == null || projectDto.getMembers().isEmpty()) {
            throw ProjectException.NO_MEMBER;
        }

        if (projectDto.getStartDate() == null || projectDto.getEndDate() == null) {
            throw ProjectException.NO_DATETIME;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date startDate = sdf.parse(projectDto.getStartDate());
        Date endDate = sdf.parse(projectDto.getEndDate());

        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setOrganizationName(projectDto.getOrganizationName());
        project.setMemberCount(projectDto.getMemberCount());
        //project.setCategories(projectDto.getCategories());
        Set<Category> categories = resolveCategories(projectDto.getCategories());
        project.setCategories(categories);
        project.setHashTags(projectDto.getHashTags());
        project.setSkills(projectDto.getSkills());
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setProjectUrlLink(projectDto.getProjectUrlLink());
        project.setVisibility(true);
        project.setCreatedAt(new Date());
        project.setUpdatedAt(new Date());

        List<ProjectUserJoin> members = projectDto.getMembers().stream().map(memberDto -> {
            return userRepository.findByEmail(memberDto.getEmail())
                    .map(user -> {
                        ProjectUserJoin join = new ProjectUserJoin();
                        join.setUser(user);
                        join.setName(memberDto.getName());
                        join.setEmail(memberDto.getEmail());
                        join.setRoles(memberDto.getRoles());
                        join.setSkills(memberDto.getSkills());
                        join.setProject(project);
                        return join;
                    })
                    .orElseThrow(() -> ProjectException.USER_NOT_FOUND);
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
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        if (projectDto.getProjectName() == null || projectDto.getProjectName().isEmpty()) {
            throw ProjectException.NO_PROJECT_NAME;
        }

        if (projectDto.getMembers() == null || projectDto.getMembers().isEmpty()) {
            throw ProjectException.NO_MEMBER;
        }

        if (projectDto.getStartDate() == null || projectDto.getEndDate() == null) {
            throw ProjectException.NO_DATETIME;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setOrganizationName(projectDto.getOrganizationName());
        project.setMemberCount(projectDto.getMemberCount());
        //project.setCategories(projectDto.getCategories());
        Set<Category> categories = resolveCategories(projectDto.getCategories());
        project.setCategories(categories);
        project.setHashTags(projectDto.getHashTags());
        project.setSkills(projectDto.getSkills());
        project.setStartDate(sdf.parse(projectDto.getStartDate()));
        project.setEndDate(sdf.parse(projectDto.getEndDate()));
        project.setProjectUrlLink(projectDto.getProjectUrlLink());
        project.setUpdatedAt(new Date());

        updateProjectMembers(project, projectDto.getMembers());

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

    private void updateProjectMembers(Project project, List<MemberDto> memberDtos) {
        List<ProjectUserJoin> existingMembers = project.getMembers();
        List<ProjectUserJoin> updatedMembers = memberDtos.stream().map(memberDto -> {
            return existingMembers.stream()
                    .filter(m -> m.getEmail().equals(memberDto.getEmail()))
                    .findFirst()
                    .map(existingMember -> {
                        updateExistingMember(existingMember, memberDto);
                        return existingMember;
                    })
                    .orElseGet(() -> createNewMember(project, memberDto));
        }).collect(Collectors.toList());

        // Remove members not in the new list
        existingMembers.removeIf(member -> updatedMembers.stream()
                .noneMatch(updatedMember -> updatedMember.getEmail().equals(member.getEmail())));

        existingMembers.addAll(updatedMembers);
    }

    private ProjectUserJoin updateExistingMember(ProjectUserJoin existingMember, MemberDto memberDto) {
        existingMember.setRoles(memberDto.getRoles());
        existingMember.setSkills(memberDto.getSkills());
        return existingMember;
    }

    private ProjectUserJoin createNewMember(Project project, MemberDto memberDto) {
        return userRepository.findByEmail(memberDto.getEmail())
                .map(user -> {
                    ProjectUserJoin join = new ProjectUserJoin();
                    join.setUser(user);
                    join.setName(memberDto.getName());
                    join.setEmail(memberDto.getEmail());
                    join.setRoles(memberDto.getRoles());
                    join.setSkills(memberDto.getSkills());
                    join.setProject(project);
                    return join;
                })
                .orElseThrow(() -> new ProjectException("Member not found with email: " + memberDto.getEmail(), ProjectErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public ProjectResponseDto deleteProject(int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        projectRepository.delete(project);
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
}
