package com.prismworks.prism.domain.project.service;

import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResultRepository;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewTotalResultRepository;
import com.prismworks.prism.domain.project.Repository.CategoryRepository;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.Repository.ProjectUserJoinRepository;
import com.prismworks.prism.domain.project.dto.*;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.domain.project.model.Category;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectCategoryJoin;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.interfaces.project.dto.request.ProjectAnonyVisibilityUpdateDto;
import com.prismworks.prism.interfaces.project.dto.request.ProjectDto;
import com.prismworks.prism.interfaces.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.infrastructure.db.user.UserJpaRepository;
import com.prismworks.prism.domain.user.service.UserService;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final CategoryRepository categoryRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectUserJoinRepository projectUserJoinRepository;
    private final PeerReviewResultRepository peerReviewResultRepository;
    private final PeerReviewTotalResultRepository peerReviewTotalResultRepository;

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
    public ProjectResponseDto createProject(UserContext userContext, ProjectDto projectDto) throws ParseException {

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
            Optional<Users> foundUser = userJpaRepository.findByEmail(memberDto.getEmail());
            ProjectUserJoin join = new ProjectUserJoin();
            join.setProject(project);
            join.setName(memberDto.getName());
            join.setEmail(memberDto.getEmail());
            join.setRoles(memberDto.getRoles());
            join.setAnonyVisibility(true);
            join.setPeerReviewDone(false);
            foundUser.ifPresentOrElse(join::setUser, () -> {});
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

        List<ProjectUserJoin> newMembers = memberDtos.stream()
                .map(memberDto -> {
                    ProjectUserJoin projectUserJoin = projectUserJoinRepository.findByEmailAndProjectId(memberDto.getEmail() ,project.getProjectId());
                    Optional<Users> foundUser = userJpaRepository.findByEmail(memberDto.getEmail());

                    ProjectUserJoin join = new ProjectUserJoin();
                    if(projectUserJoin == null) {
                        join.setProject(project);
                        join.setName(memberDto.getName());
                        join.setEmail(memberDto.getEmail());
                        join.setRoles(memberDto.getRoles());
                        join.setAnonyVisibility(true);
                        join.setPeerReviewDone(false);
                        //join.setPeerReviewDone(projectUserJoin.isPeerReviewDone());
                    }else {
                        if (foundUser.isPresent()) {
                            join.setProject(project);
                            join.setUser(foundUser.get());
                            join.setName(projectUserJoin.getName());
                            join.setEmail(projectUserJoin.getEmail());
                            join.setRoles(memberDto.getRoles());
                            join.setAnonyVisibility(projectUserJoin.getAnonyVisibility());
                            join.setPeerReviewDone(projectUserJoin.isPeerReviewDone());
                        }else{
                            join.setProject(project);
                            join.setName(memberDto.getName());
                            join.setEmail(memberDto.getEmail());
                            join.setRoles(memberDto.getRoles());
                            join.setAnonyVisibility(projectUserJoin.getAnonyVisibility());
                            join.setPeerReviewDone(projectUserJoin.isPeerReviewDone());
                        }
                    }
                    return join;
                })
                .toList();

        if (project.getMembers() != null) {
            project.getMembers().clear();
            projectRepository.flush();
        }

        project.getMembers().addAll(newMembers);
    }


    private ProjectUserJoin updateExistingMember(ProjectUserJoin existingMember, MemberDto memberDto) {
        existingMember.setRoles(memberDto.getRoles());
        return existingMember;
    }

    private ProjectUserJoin createNewMember(Project project, MemberDto memberDto) {
        return userJpaRepository.findByEmail(memberDto.getEmail())
                .map(user -> {
                    ProjectUserJoin join = new ProjectUserJoin();
                    join.setUser(user);
                    join.setName(memberDto.getName());
                    join.setEmail(memberDto.getEmail());
                    join.setRoles(memberDto.getRoles());
                    join.setProject(project);
                    join.setPeerReviewDone(false);
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

        return projects.stream()
                .map(project -> convertToSummaryDtoForGetMeInvolvedProjects(myEmail, project))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getWhoInvolvedProjects(String userId) {
        List<Project> projects = projectRepository.findByMemberUserId(userId);

        return projects.stream()
                .map(project -> convertToSummaryDtoForWhoInvolvedProjects(userId,project))
                .collect(Collectors.toList());

    }
    private SummaryProjectDto convertToSummaryDtoForWhoInvolvedProjects(String userId,Project project) {
        PeerReviewTotalResult peerReviewTotalResult = peerReviewTotalResultRepository.findByProjectIdAndUserIdAndPrismType(
                project.getProjectId(), userId, "each"
        );
        int surveyParticipant = projectUserJoinRepository.getSurveyParticipant(project.getProjectId());

        String evaluation = "";

        if(peerReviewTotalResult == null){
            evaluation = "총평 데이터 없음";
        }else{
            evaluation = peerReviewTotalResult.getEvalution();
        }

        return SummaryProjectDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .urlVisibility(project.getUrlVisibility())
                .userEvaluation(evaluation)
                .surveyParticipants(surveyParticipant)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getMeRegisteredProjects(String myEmail) {
        List<Project> projects = projectRepository.findByOwnerEmail(myEmail);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    private SummaryProjectDto convertToSummaryDto(Project project) {
        int surveyParticipant = projectUserJoinRepository.getSurveyParticipant(project.getProjectId());

        return SummaryProjectDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .urlVisibility(project.getUrlVisibility())
                .userEvaluation("Sample Evaluation")
                .surveyParticipants(surveyParticipant)
                .build();
    }

    private SummaryProjectDto convertToSummaryDtoForGetMeInvolvedProjects(String myEmail, Project project) {
        boolean anonyVisibility = projectRepository.findByAnonyVisibility(project.getProjectId(), myEmail);
        PeerReviewTotalResult peerReviewTotalResult = peerReviewTotalResultRepository.findByProjectIdAndEmailAndPrismType(
                project.getProjectId(), myEmail, "each"
        );
        int surveyParticipant = projectUserJoinRepository.getSurveyParticipant(project.getProjectId());

        String evaluation;

        if(peerReviewTotalResult == null){
            evaluation = "총평 데이터 없음";
        }else{
            evaluation = peerReviewTotalResult.getEvalution();
        }

        return SummaryProjectDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .categories(project.getCategories().stream()
                        .map(c -> c.getCategory().getName())
                        .collect(Collectors.toList()))
                .urlVisibility(project.getUrlVisibility())
                .userEvaluation(evaluation)
                .surveyParticipants(surveyParticipant)
                .anonyVisibility(anonyVisibility)
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

        if (project.getMembers().stream().noneMatch(member -> member.getEmail().equals(myEmail))) {
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
                .urlVisibility(project.getUrlVisibility())
                .projectDescription(project.getProjectDescription())
                .mostCommonTraits("계산로직 미구현 하드코딩")
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
                        UserDto.UserProfileDetail userProfileDetail = userService.getUserProfileDetail(member.getUser().getUserId());

                        return MemberDetailDto.builder()
                            .name(userProfileDetail.getUsername())
                            .email(userProfileDetail.getEmail())
                            .interestDomains(userProfileDetail.getInterestJobs())
                            .introduction(userProfileDetail.getIntroduction())
                            .roles(member.getRoles()) // 역할 정보 설정
                            .projectCount(getProjectCount(member.getUser().getUserId())) // 프로젝트 수 계산
                            .build();
                    }
                })
                .collect(Collectors.toList());

        long anonymousCount = memberDetails.stream().filter(MemberDetailDto::isAnonyVisibility).count();

        return ProjectDetailDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .organizationName(project.getOrganizationName())
                .startDate(formatDate(project.getStartDate()))
                .endDate(formatDate(project.getEndDate()))
                .projectUrlLink(project.getProjectUrlLink())
                .urlVisibility(project.getUrlVisibility())
                .projectDescription(project.getProjectDescription())
                .mostCommonTraits("")
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

        Optional<PeerReviewResult> optionalPeerReviewResult = peerReviewResultRepository.findByEmailAndPrismType(userContext.getEmail(), "each");
        if (optionalPeerReviewResult.isPresent()) {
            PeerReviewResult peerReviewResult = optionalPeerReviewResult.get();
            peerReviewResult.setUserId(userContext.getUserId());
            peerReviewResultRepository.save(peerReviewResult);
        }

        optionalPeerReviewResult = peerReviewResultRepository.findByEmailAndPrismType(userContext.getEmail(), "total");
        if (optionalPeerReviewResult.isPresent()) {
            PeerReviewResult peerReviewResult = optionalPeerReviewResult.get();
            peerReviewResult.setUserId(userContext.getUserId());
            peerReviewResultRepository.save(peerReviewResult);
        }

        Optional<PeerReviewTotalResult> optionalPeerReviewTotalResult = peerReviewTotalResultRepository.findByEmailAndPrismType(userContext.getEmail(), "each");
        if (optionalPeerReviewTotalResult.isPresent()) {
            PeerReviewTotalResult peerReviewTotalResult = optionalPeerReviewTotalResult.get();
            peerReviewTotalResult.setUserId(userContext.getUserId());
            peerReviewTotalResultRepository.save(peerReviewTotalResult);
        }

        optionalPeerReviewTotalResult = peerReviewTotalResultRepository.findByEmailAndPrismType(userContext.getEmail(), "total");
        if (optionalPeerReviewTotalResult.isPresent()) {
            PeerReviewTotalResult peerReviewTotalResult = optionalPeerReviewTotalResult.get();
            peerReviewTotalResult.setUserId(userContext.getUserId());
            peerReviewTotalResultRepository.save(peerReviewTotalResult);
        }

        ProjectUserJoin updatedMember = project.getMembers().stream()
                .filter(member -> member.getEmail().equals(anonymousEmail))
                .findFirst()
                .orElseThrow(() -> new ProjectException("Anonymous member not found", ProjectErrorCode.NO_MEMBER));


        updatedMember.setEmail(userContext.getEmail());
        userJpaRepository.findById(userContext.getUserId())
                .ifPresentOrElse(updatedMember::setUser, () -> {
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
                .filter(MemberDetailDto::isAnonyVisibility)
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
    public ProjectAnonyVisibilityUpdateDto updateProjectUserJoinVisibility(int projectId, String email, boolean anonyVisibility) {
        ProjectUserJoin projectUserJoin = projectUserJoinRepository.findByEmailAndProjectId(email, projectId);

        projectUserJoin.setAnonyVisibility(anonyVisibility);
        projectUserJoinRepository.save(projectUserJoin); // 변경 사항을 DB에 저장

        return ProjectAnonyVisibilityUpdateDto.builder()
                .projectId(projectUserJoin.getProject().getProjectId())
                .anonyVisibility(projectUserJoin.getAnonyVisibility()) // projectUserJoin의 anonyVisibility 반환
                .build();
    }

    @Transactional(readOnly = true)
    public Long countUserInProject(Integer projectId) {
        return projectRepository.countUserByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<ProjectUserJoin> getAllMemberInProject(Integer projectId) {
        return projectRepository.findAllMemberByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public Project getProjectByOwner(Integer projectId, String email) {
        return projectRepository.findByProjectIdAndCreatedBy(projectId, email)
                .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public ProjectPeerReviewEmailInfoDto getProjectPeerReviewEmailInfo(Integer projectId, String ownerEmail) {
        Project project = this.getProjectByOwner(projectId, ownerEmail);

        String ownerName = "";
        List<String> notReviewingMemberEmails = new ArrayList<>();
        for(ProjectUserJoin member: project.getMembers()) {
            if(!member.isPeerReviewDone()) {
                notReviewingMemberEmails.add(member.getEmail());
            }

            if(member.getEmail().equals(ownerEmail)) {
                ownerName = member.getName();
            }
        }

        return ProjectPeerReviewEmailInfoDto.builder()
                .projectId(projectId)
                .projectName(project.getProjectName())
                .ownerName(ownerName)
                .notReviewingMemberEmails(notReviewingMemberEmails)
                .build();
    }

    public List<MemberDetailDto> getProjectMembers(Integer projectId) {
        List<ProjectUserJoin> projectUserJoins = projectUserJoinRepository.findByProjectId(projectId);

        projectUserJoins.forEach(join -> {
            Hibernate.initialize(join.getRoles());
        });

        return projectUserJoins.stream()
            .map(join -> {

                UserDto.UserProfileDetail userProfileDetail = userService.getUserProfileDetail(join.getUser().getUserId());

                return MemberDetailDto.builder()
                    .name(userProfileDetail.getUsername())
                    .email(userProfileDetail.getEmail())
                    .interestDomains(userProfileDetail.getInterestJobs())
                    .introduction(userProfileDetail.getIntroduction())
                    .roles(join.getRoles())
                    .projectCount(getProjectCount(join.getUser().getUserId()))
                    .build();
            })
            .collect(Collectors.toList());
    }

    private int getProjectCount(String userId) {
        return projectUserJoinRepository.countByUserId(userId);
    }

    @Transactional
    public void memberDonePeerReview(Integer projectId, String reviewerEmail) {
        ProjectUserJoin member = projectUserJoinRepository.findByEmailAndProjectId(reviewerEmail, projectId);
        member.doneReview();
    }
}
