package com.prismworks.prism.domain.project.service;

import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.project.Repository.CategoryRepository;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.dto.*;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.domain.project.model.Category;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectCategoryJoin;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public Category saveCategoryTransactional(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    return categoryRepository.save(newCategory);
                });
    }

    @Transactional
    public void resolveCategoryJoins(Project project, List<String> categoryNames) {
        for (String name : categoryNames) {
            Category category = saveCategoryTransactional(name); // 트랜잭션 메서드 사용
            ProjectCategoryJoin join = new ProjectCategoryJoin();
            join.setProject(project);
            join.setCategory(category);
            project.getCategories().add(join);
        }
    }

    @Transactional
    public ProjectResponseDto createProject(UserContext userContext,ProjectDto projectDto) throws ParseException {

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
        project.setSkills(projectDto.getSkills());
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setProjectUrlLink(projectDto.getProjectUrlLink());
        project.setCreatedBy(userContext.getEmail());
        project.setUrlVisibility(true);
        project.setCreatedAt(new Date());
        project.setUpdatedAt(new Date());

        projectRepository.save(project);  // 먼저 프로젝트를 저장합니다.

        resolveCategoryJoins(project, projectDto.getCategories());

        List<ProjectUserJoin> members = projectDto.getMembers().stream().map(memberDto -> {
            Optional<Users> foundUser = userRepository.findByEmail(memberDto.getEmail());
            ProjectUserJoin join = new ProjectUserJoin();
            join.setProject(project);
            join.setName(memberDto.getName());
            join.setEmail(memberDto.getEmail());
            join.setRoles(memberDto.getRoles());
            join.setAnonyVisibility(true);
            foundUser.ifPresentOrElse(join::setUser, () -> {;});
            return join;
        }).collect(Collectors.toList());

        project.setMembers(members);
        projectRepository.save(project);  // 다시 프로젝트를 저장하여 카테고리와 멤버를 포함시킵니다.

        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .organizationName(project.getOrganizationName())
                .memberCount(project.getMemberCount())
                .categories(project.getCategories())
                .skills(project.getSkills())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .projectUrlLink(project.getProjectUrlLink())
                .build();
    }

    @Transactional
    public ProjectResponseDto updateProject(String myEmail,int projectId, ProjectDto projectDto) throws ParseException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        if (!project.getCreatedBy().equals(myEmail)) {
            throw new ProjectException("You do not have permission to update this project", ProjectErrorCode.UNAUTHORIZED);
        }

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
        project.setSkills(projectDto.getSkills());
        project.setStartDate(sdf.parse(projectDto.getStartDate()));
        project.setEndDate(sdf.parse(projectDto.getEndDate()));
        project.setProjectUrlLink(projectDto.getProjectUrlLink());
        project.setUrlVisibility(projectDto.isUrlVisibility());
        project.setUpdatedAt(new Date());

        project.getCategories().clear();
        resolveCategoryJoins(project, projectDto.getCategories());

        updateProjectMembers(project, projectDto.getMembers());

        projectRepository.save(project);

        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .organizationName(project.getOrganizationName())
                .memberCount(project.getMemberCount())
                .categories(project.getCategories())
                .skills(project.getSkills())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .projectUrlLink(project.getProjectUrlLink())
                .urlVisibility(project.getUrlVisibility())
                .createdBy(project.getCreatedBy())
                .build();
    }

    @Transactional
    public void updateProjectMembers(Project project, List<MemberDto> memberDtos) {
        if (project.getMembers() != null) {
            project.getMembers().clear();
            projectRepository.flush();
        }

        List<ProjectUserJoin> newMembers = memberDtos.stream()
                .map(memberDto -> {
                    Optional<Users> foundUser = userRepository.findByEmail(memberDto.getEmail());
                    ProjectUserJoin join = new ProjectUserJoin();
                    join.setProject(project);
                    join.setName(memberDto.getName());
                    join.setEmail(memberDto.getEmail());
                    join.setRoles(memberDto.getRoles());
                    join.setAnonyVisibility(memberDto.isAnonyVisibility());
                    foundUser.ifPresent(join::setUser);
                    return join;
                })
                .collect(Collectors.toList());

        project.getMembers().addAll(newMembers);
    }


    private ProjectUserJoin updateExistingMember(ProjectUserJoin existingMember, MemberDto memberDto) {
        existingMember.setRoles(memberDto.getRoles());
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
                    join.setProject(project);
                    return join;
                })
                .orElseThrow(() -> new ProjectException("Member not found with email: " + memberDto.getEmail(), ProjectErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public ProjectResponseDto deleteProject(String myEmail, int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        if (!project.getCreatedBy().equals(myEmail)) {
            throw new ProjectException("You do not have permission to delete this project", ProjectErrorCode.UNAUTHORIZED);
        }

        projectRepository.delete(project);
        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .organizationName(project.getOrganizationName())
                .memberCount(project.getMemberCount())
                .categories(project.getCategories())
                .skills(project.getSkills())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .projectUrlLink(project.getProjectUrlLink())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getProjectSummaryByName(String projectName) {
        List<Project> projects = projectRepository.findByProjectName(projectName);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getProjectSummaryByMemberAndFilters(String projectName, String memberName, List<String> categories, String organizationName) {
        List<Project> projects = projectRepository.findByFilters(projectName, memberName, categories, organizationName);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getMeInvolvedProjects(String myEmail) {
        List<Project> projects = projectRepository.findByMemberEmail(myEmail);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getWhoInvolvedProjects(String userId) {
        List<Project> projects = projectRepository.findByMemberUserId(userId);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getMeRegisteredProjects(String myEmail) {
        List<Project> projects = projectRepository.findByOwnerEmail(myEmail);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    private SummaryProjectDto convertToSummaryDto(Project project) {
        return SummaryProjectDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .urlVisibility(project.getUrlVisibility())
                .userEvaluation("Sample Evaluation")
                .surveyParticipants(0)
                .build();
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    @Transactional(readOnly = true)
    public ProjectDetailDto getProjectDetailInMyPage(String myEmail, int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        if (!project.getMembers().stream().anyMatch(member -> member.getEmail().equals(myEmail))) {
            throw new ProjectException("You are not a member of this project", ProjectErrorCode.UNAUTHORIZED);
        }

        project.getMembers().forEach(member -> member.getRoles().size());

        return convertToDetailDtoInMyPage(project);
    }

    private ProjectDetailDto convertToDetailDtoInMyPage(Project project) {
        List<MemberDetailDto> memberDetails = project.getMembers().stream()
                .map(member -> {
                    // User 객체가 null인 경우를 처리
                    if (member.getUser() == null) {
                        // 로깅, 오류 처리 또는 기본값 설정
                        return new MemberDetailDto("-1", member.getName(), member.getEmail(), member.getRoles(),member.getAnonyVisibility());
                    } else {
                        return new MemberDetailDto(member.getUser().getUserId(), member.getName(), member.getEmail(), member.getRoles(),member.getAnonyVisibility());
                    }
                })
                .collect(Collectors.toList());

        long anonymousCount = memberDetails.stream().filter(MemberDetailDto::isAnonyVisibility).count();

        return ProjectDetailDto.builder()
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .projectUrlLink(project.getProjectUrlLink())
                .projectDescription(project.getProjectDescription())
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .skills(project.getSkills())
                .members(memberDetails)
                .anonymousCount(anonymousCount)
                .build();
    }


    @Transactional(readOnly = true)
    public ProjectDetailDto getProjectDetailInRetrieve(int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException("Project not found", ProjectErrorCode.PROJECT_NOT_FOUND));

        Hibernate.initialize(project.getMembers());
        project.getMembers().forEach(member -> Hibernate.initialize(member.getRoles()));
        Hibernate.initialize(project.getCategories());

        return convertToDetailDtoInRetrieve(project);
    }

    private ProjectDetailDto convertToDetailDtoInRetrieve(Project project) {
        List<MemberDetailDto> memberDetails = project.getMembers().stream()
                .map(member -> {
                    // User 객체가 null인 경우를 처리
                    if (member.getUser() == null) {
                        // 로깅, 오류 처리 또는 기본값 설정
                        return new MemberDetailDto("-1", member.getName(), member.getEmail(), member.getRoles(), member.getAnonyVisibility());
                    } else {
                        return new MemberDetailDto(member.getUser().getUserId(), member.getName(), member.getEmail(), member.getRoles(), member.getAnonyVisibility());
                    }
                })
                .collect(Collectors.toList());

        long anonymousCount = memberDetails.stream().filter(MemberDetailDto::isAnonyVisibility).count();

        return ProjectDetailDto.builder()
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .projectUrlLink(project.getProjectUrlLink())
                .projectDescription(project.getProjectDescription())
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .skills(project.getSkills())
                .members(memberDetails)
                .anonymousCount(anonymousCount)
                .build();
    }

    @Transactional
    public ProjectDetailDto linkAnonymousProjectToUserAccount(UserContext userContext, int projectId, String anonymousEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException("Project not found", ProjectErrorCode.PROJECT_NOT_FOUND));

        ProjectUserJoin updatedMember = project.getMembers().stream()
                .filter(member -> member.getEmail().equals(anonymousEmail))
                .findFirst()
                .orElseThrow(() -> new ProjectException("Anonymous member not found", ProjectErrorCode.NO_MEMBER));

        updatedMember.setEmail(userContext.getEmail());
        userRepository.findById(userContext.getUserId())
                .ifPresentOrElse(user -> {
                    updatedMember.setUser(user);
                }, () -> {
                    throw new ProjectException("User not found with given ID", ProjectErrorCode.USER_NOT_FOUND);
                });

        projectRepository.save(project);
        return createProjectDetailDto(project);
    }

    private ProjectDetailDto createProjectDetailDto(Project project) {
        project.getMembers().forEach(member -> {
            Hibernate.initialize(member.getRoles());
        });

        List<MemberDetailDto> memberDetails = project.getMembers().stream()
                .map(member -> new MemberDetailDto(
                        member.getUser() != null ? member.getUser().getUserId() : "-1",
                        member.getName(),
                        member.getEmail(),
                        member.getRoles(),
                        member.getAnonyVisibility()
                ))
                .collect(Collectors.toList());

        long anonymousCount = memberDetails.stream()
                .filter(member -> member.getUserId().equals("-1"))
                .count();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return ProjectDetailDto.builder()
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(sdf.format(project.getStartDate()))
                .endDate(sdf.format(project.getEndDate()))
                .projectUrlLink(project.getProjectUrlLink())
                .projectDescription(project.getProjectDescription())
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .skills(project.getSkills())
                .members(memberDetails)
                .anonymousCount(anonymousCount)
                .build();
    }



    @Transactional
    public ProjectVisibilityUpdateDto updateProjectVisibility(int projectId, boolean visibility) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("Project not found"));

        project.setUrlVisibility(visibility);
        projectRepository.save(project);

        return ProjectVisibilityUpdateDto.builder()
                .projectId(project.getProjectId())
                .visibility(project.getUrlVisibility())
                .build();
    }

    @Transactional(readOnly = true)
    public Long countUserInProject(Integer projectId) {
        return projectRepository.countUserByProjectId(projectId);
    }
}
