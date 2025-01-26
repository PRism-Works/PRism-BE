package com.prismworks.prism.domain.project.service;

import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewRepository;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.dto.*;
import com.prismworks.prism.domain.project.dto.command.CreateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectUserJoinsCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectCategoryJoin;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.user.dto.UserDetailInfo;
import com.prismworks.prism.domain.user.repository.UserRepository;
import com.prismworks.prism.interfaces.project.dto.request.ProjectAnonyVisibilityUpdateDto;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.service.UserService;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserRepository userRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    private final PeerReviewRepository peerReviewRepository;
    // private final PeerReviewTotalResultJpaRepository peerReviewTotalResultJpaRepository;

    @Transactional
    public void resolveCategoryJoins(Project project, List<String> categoryNames) {
        project.getCategories().clear();

        project.getCategories().addAll(
            categoryNames.stream()
                .map(projectRepository::getCategoryByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(category -> {
                    ProjectCategoryJoin join = new ProjectCategoryJoin();
                    join.setProject(project);
                    join.setCategory(category);
                    return join;
                }).toList()
        );
    }

    @Transactional
    public ProjectDetailInfo createProject(CreateProjectCommand command) {

        Project project = new Project(command);
        projectRepository.saveProject(project);

        resolveCategoryJoins(project, command.getCategories());

        project.updateMembers(mapToProjectMembers(project, command.getMembers()));

        return new ProjectDetailInfo(project);
    }

    @Transactional
    public ProjectDetailInfo updateProject(UpdateProjectCommand command) {
        Project project = this.getProjectById(command.getProjectId());

        // M:N 관계로 매핑된 Entity들에 대한 로직
        resolveCategoryJoins(project, command.getCategories());

        project.updateProject(command, mapToProjectMembers(project, command.getMembers()));

        return new ProjectDetailInfo(project);
    }

    @Transactional
    public List<ProjectUserJoin> mapToProjectMembers(Project project, List<UpdateProjectUserJoinsCommand> members) {

        return members.stream()
                .map(memberDto -> {
                    ProjectUserJoin projectUserJoin = projectRepository.getMemberByEmailAndProjectId(project.getProjectId(), memberDto.getEmail());
                    Optional<Users> foundUser = userRepository.getUserByEmail(memberDto.getEmail());

                    ProjectUserJoin join = new ProjectUserJoin();
                    join.setProject(project);
                    join.setRoles(memberDto.getRoles());

                    if(projectUserJoin == null) {
                        join.setName(memberDto.getName());
                        join.setEmail(memberDto.getEmail());
                        join.setAnonyVisibility(true);
                        join.setPeerReviewDone(false);
                    }else {
                        foundUser.ifPresentOrElse(
                            user -> {
                                join.setUser(user);
                                join.setName(projectUserJoin.getName());
                                join.setEmail(projectUserJoin.getEmail());
                            },
                            () -> {
                                join.setName(memberDto.getName());
                                join.setEmail(memberDto.getEmail());
                            }
                        );

                        join.setAnonyVisibility(projectUserJoin.getAnonyVisibility());
                        join.setPeerReviewDone(projectUserJoin.isPeerReviewDone());
                    }
                    return join;
                })
                .toList();
    }

    @Transactional
    public void deleteProject(String myEmail, int projectId) {
        Project project = this.getProjectById(projectId);

        if (!project.getCreatedBy().equals(myEmail)) {
            throw new ProjectException("You do not have permission to delete this project", ProjectErrorCode.UNAUTHORIZED);
        }

        this.deleteProject(project);
    }

    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getProjectSummaryByName(String projectName) {
        List<Project> projects = projectRepository.getProjectsByName(projectName);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getProjectSummaryByMemberAndFilters(String projectName, String memberName, List<String> categories, String organizationName) { //TODO Input Dto로 만들기
        List<Project> projects = projectRepository.getProjectsByFilters(projectName, memberName, categories, organizationName);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getMeInvolvedProjects(String myEmail) {
        List<Project> projects = projectRepository.getProjectsByMemberEmail(myEmail);

        return projects.stream()
                .map(project -> convertToSummaryDtoForGetMeInvolvedProjects(myEmail, project))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getWhoInvolvedProjects(String userId) {
        List<Project> projects = projectRepository.getProjectsByMemberId(userId);

        return projects.stream()
                .map(project -> convertToSummaryDtoForWhoInvolvedProjects(userId,project))
                .collect(Collectors.toList());

    }
    private SummaryProjectDto convertToSummaryDtoForWhoInvolvedProjects(String userId,Project project) {
        PeerReviewTotalResult peerReviewTotalResult = peerReviewRepository.getPeerReviewTotalResultByProjectIdAndUserIdAndPrismType(
                project.getProjectId(), userId, "each"
        );
        int numOfsurveyParticipant = projectRepository.countSurveyParticipantsByProjectId(project.getProjectId());

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
                .categories(project.getCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()))
                .urlVisibility(project.getUrlVisibility())
                .userEvaluation(evaluation)
                .surveyParticipants(numOfsurveyParticipant)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SummaryProjectDto> getMeRegisteredProjects(String myEmail) {
        List<Project> projects = projectRepository.getProjectsByOwnerEmail(myEmail);
        return projects.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    private SummaryProjectDto convertToSummaryDto(Project project) {
        int surveyParticipant = projectRepository.countSurveyParticipantsByProjectId(project.getProjectId());

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
        boolean anonyVisibility = projectRepository.getAnonyVisibilityByProjectIdAndEmail(project.getProjectId(), myEmail);
        PeerReviewTotalResult peerReviewTotalResult = peerReviewRepository.getPeerReviewTotalResultByProjectIdAndEmailAndPrismType(
                project.getProjectId(), myEmail, "each"
        );
        int surveyParticipant = projectRepository.countSurveyParticipantsByProjectId(project.getProjectId());

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
        Project project = this.getProjectById(projectId);

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
        Project project = this.getProjectById(projectId);

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
                        UserDetailInfo userProfileDetail = userService.getUserDetailInfo(member.getUser().getUserId());

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
        Project project = this.getProjectById(projectId);

        Optional<PeerReviewResult> optionalPeerReviewResult = peerReviewRepository.getPeerReviewResultByEmailAndPrismType(userContext.getEmail(), "each");
        if (optionalPeerReviewResult.isPresent()) {
            PeerReviewResult peerReviewResult = optionalPeerReviewResult.get();
            peerReviewResult.setUserId(userContext.getUserId());
            peerReviewRepository.savePeerReviewResult(peerReviewResult);
        }

        optionalPeerReviewResult = peerReviewRepository.getPeerReviewResultByEmailAndPrismType(userContext.getEmail(), "total");
        if (optionalPeerReviewResult.isPresent()) {
            PeerReviewResult peerReviewResult = optionalPeerReviewResult.get();
            peerReviewResult.setUserId(userContext.getUserId());
            peerReviewRepository.savePeerReviewResult(peerReviewResult);
        }

        Optional<PeerReviewTotalResult> optionalPeerReviewTotalResult = peerReviewRepository.getPeerReviewTotalResultByEmailAndPrismType(userContext.getEmail(), "each");
        if (optionalPeerReviewTotalResult.isPresent()) {
            PeerReviewTotalResult peerReviewTotalResult = optionalPeerReviewTotalResult.get();
            peerReviewTotalResult.setUserId(userContext.getUserId());
            peerReviewRepository.savePeerReviewTotalResult(peerReviewTotalResult);
        }

        optionalPeerReviewTotalResult = peerReviewRepository.getPeerReviewTotalResultByEmailAndPrismType(userContext.getEmail(), "total");
        if (optionalPeerReviewTotalResult.isPresent()) {
            PeerReviewTotalResult peerReviewTotalResult = optionalPeerReviewTotalResult.get();
            peerReviewTotalResult.setUserId(userContext.getUserId());
            peerReviewRepository.savePeerReviewTotalResult(peerReviewTotalResult);
        }

        ProjectUserJoin updatedMember = project.getMembers().stream()
                .filter(member -> member.getEmail().equals(anonymousEmail))
                .findFirst()
                .orElseThrow(() -> new ProjectException("Anonymous member not found", ProjectErrorCode.NO_MEMBER));


        updatedMember.setEmail(userContext.getEmail());
        userRepository.getUserById(userContext.getUserId())
                .ifPresentOrElse(updatedMember::setUser, () -> {
                    throw new ProjectException("User not found with given ID", ProjectErrorCode.USER_NOT_FOUND);
                });

        projectRepository.saveProject(project);
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
        ProjectUserJoin projectUserJoin = projectRepository.getMemberByEmailAndProjectId(projectId, email);

        projectUserJoin.setAnonyVisibility(anonyVisibility);
        projectRepository.saveProjectMember(projectUserJoin); // 변경 사항을 DB에 저장

        return ProjectAnonyVisibilityUpdateDto.builder()
                .projectId(projectUserJoin.getProject().getProjectId())
                .anonyVisibility(projectUserJoin.getAnonyVisibility()) // projectUserJoin의 anonyVisibility 반환
                .build();
    }

    @Transactional(readOnly = true)
    public Long countUserInProject(Integer projectId) {
        return projectRepository.getMemberCountByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<ProjectUserJoin> getAllMemberInProject(Integer projectId) { //TODO Output Dto로 변환
        return projectRepository.getMembersByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public Project getProjectByOwner(Integer projectId, String email) {
        return this.getProjectByIdAndCreator(projectId, email);
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
        List<ProjectUserJoin> projectUserJoins = projectRepository.getMembersByProjectId(projectId);

        projectUserJoins.forEach(join -> {
            Hibernate.initialize(join.getRoles());
        });

        return projectUserJoins.stream()
            .map(join -> {

                UserDetailInfo userProfileDetail = userService.getUserDetailInfo(join.getUser().getUserId());

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
        return projectRepository.getProjectCountByUserId(userId);
    }

    @Transactional
    public void memberDonePeerReview(int projectId, String reviewerEmail) {
        ProjectUserJoin member = projectRepository.getMemberByEmailAndProjectId(projectId, reviewerEmail);
        member.doneReview();
    }

    private Project getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId)
            .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
    }

    private void deleteProject(Project project) {
        projectRepository.deleteProject(project);
    }

    private Project getProjectByIdAndCreator(int projectId, String email) {
        return projectRepository.getProjectByIdAndCreator(projectId, email)
            .orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
    }
}
