package com.prismworks.prism.domain.project.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewRepository;
import com.prismworks.prism.domain.project.Repository.ProjectRepository;
import com.prismworks.prism.domain.project.dto.ProjectDetailInfo;
import com.prismworks.prism.domain.project.dto.ProjectInfo;
import com.prismworks.prism.domain.project.dto.ProjectMemberInfo;
import com.prismworks.prism.domain.project.dto.ProjectPeerReviewEmailInfoDto;
import com.prismworks.prism.domain.project.dto.command.CreateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.ProjectMemberCommonCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.dto.query.ProjectInfoQuery;
import com.prismworks.prism.domain.project.exception.ProjectErrorCode;
import com.prismworks.prism.domain.project.exception.ProjectException;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.model.ProjectUserJoin;
import com.prismworks.prism.domain.user.dto.UserDetailInfo;
import com.prismworks.prism.domain.user.repository.UserRepository;
import com.prismworks.prism.domain.user.service.UserService;
import com.prismworks.prism.interfaces.project.dto.request.ProjectAnonyVisibilityUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    private final PeerReviewRepository peerReviewRepository;

    @Transactional
    public ProjectDetailInfo createProject(CreateProjectCommand command) {
        List<ProjectMemberCommonCommand> memberCommands = command.getMembers();// todo: facade로 빼기
        memberCommands.forEach(memberCommand ->
            userRepository.getUserByEmail(memberCommand.getEmail())
                .ifPresent(memberCommand::setUser));

        command.getCategories().forEach(
            categoryCommand ->
                projectRepository.getCategoryByName(categoryCommand.getCategoryName())
                    .ifPresent(categoryCommand::setCategory));

        Project project = new Project(command);
        projectRepository.saveProject(project);

        return new ProjectDetailInfo(project);
    }

    @Transactional
    public ProjectDetailInfo updateProject(UpdateProjectCommand command) {
        Project project = this.getProjectById(command.getProjectId());
        List<ProjectMemberCommonCommand> memberCommands = command.getMembers();// todo: facade로 빼기
        memberCommands.forEach(memberCommand ->
			userRepository.getUserByEmail(memberCommand.getEmail())
                .ifPresent(memberCommand::setUser));

        command.getCategories().forEach(
            categoryCommand ->
                projectRepository.getCategoryByName(categoryCommand.getCategoryName())
                    .ifPresent(categoryCommand::setCategory));

        project.updateProject(command);

        return new ProjectDetailInfo(project);
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
    public List<ProjectInfo> getProjectSummaryByName(String projectName) {
        List<Project> projects = projectRepository.getProjectsByName(projectName);
        return projects.stream()
            .map(project -> buildProjectInfo(project, null, false))
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProjectInfo> getProjectSummaryByMemberAndFilters(ProjectInfoQuery query) {
        List<Project> projects = projectRepository.getProjectsByFilters(query);
        return projects.stream()
            .map(project -> buildProjectInfo(project, null, false))
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProjectInfo> getMeRegisteredProjects(String myEmail) {
        List<Project> projects = projectRepository.getProjectsByOwnerEmail(myEmail);
        return projects.stream()
            .map(project -> buildProjectInfo(project, null, false))
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProjectInfo> getMeInvolvedProjects(String myEmail) {
        List<Project> projects = projectRepository.getProjectsByMemberEmail(myEmail);

        return projects.stream()
            .map(project -> {
                boolean anonyVisibility = projectRepository.getAnonyVisibilityByProjectIdAndEmail(project.getProjectId(), myEmail);
                PeerReviewTotalResult peerReviewTotalResult = peerReviewRepository.getPeerReviewTotalResultByProjectIdAndEmailAndPrismType(
                    project.getProjectId(), myEmail, "each"
                );
                return buildProjectInfo(project, peerReviewTotalResult, anonyVisibility);
            })
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProjectInfo> getWhoInvolvedProjects(String userId) {
        List<Project> projects = projectRepository.getProjectsByMemberId(userId);

        return projects.stream()
            .map(project -> {
                PeerReviewTotalResult peerReviewTotalResult = peerReviewRepository.getPeerReviewTotalResultByProjectIdAndUserIdAndPrismType(
                    project.getProjectId(), userId, "each"
                );
                return buildProjectInfo(project, peerReviewTotalResult, false);
            })
            .collect(Collectors.toList());

    }

    private ProjectInfo buildProjectInfo(Project project, PeerReviewTotalResult peerReviewTotalResult, boolean anonyVisibility) {
        int surveyParticipant = projectRepository.countSurveyParticipantsByProjectId(project.getProjectId());
        String evaluation = (peerReviewTotalResult == null) ? "총평 데이터 없음" : peerReviewTotalResult.getEvalution();

        return ProjectInfo.builder()
            .projectId(project.getProjectId())
            .projectName(project.getProjectName())
            .projectDescription(project.getProjectDescription())
            .organizationName(project.getOrganizationName())
            .startDate(formatDate(project.getStartDate()))
            .endDate(formatDate(project.getEndDate()))
            .projectUrlLink(project.getProjectUrlLink())
            .categories(project.getCategories().stream()
                .map(c -> c.getCategory().getName())
                .collect(Collectors.toList()))
            .skills(project.getSkills())
            .userEvaluation(evaluation)
            .surveyParticipants(surveyParticipant)
            .anonyVisibility(anonyVisibility)
            .build();
    }

    private String formatDate(Date date) {  // TODO Util로 빼서 공통으로 사용
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Transactional(readOnly = true)
    public ProjectDetailInfo getProjectDetailInMyPage(String myEmail, int projectId) {
        Project project = this.getProjectById(projectId);

        if (project.getMembers().stream().noneMatch(member -> member.getEmail().equals(myEmail))) {
            throw new ProjectException("You are not a member of this project", ProjectErrorCode.UNAUTHORIZED);
        }

        return getProjectDetailInfo(project);
    }

    @Transactional(readOnly = true)
    public ProjectDetailInfo getProjectDetailInRetrieve(int projectId) {
        Project project = this.getProjectById(projectId);

        return getProjectDetailInfo(project);
    }

    private ProjectDetailInfo getProjectDetailInfo(Project project) {
        List<ProjectMemberInfo> projectMemberInfos = project.getMembers()
            .stream()
            .map(this::getProjectMemberInfo)
            .toList();

        long anonymousCount = projectMemberInfos.stream().filter(ProjectMemberInfo::isAnonyVisibility).count();

        return new ProjectDetailInfo(project, projectMemberInfos, anonymousCount);
    }

    @Transactional
    public ProjectDetailInfo linkAnonymousProjectToUserAccount(UserContext userContext, int projectId, String anonymousEmail) {
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
        return createProjectDetailInfo(project);
    }

    private ProjectDetailInfo createProjectDetailInfo(Project project) {
        List<ProjectMemberInfo> projectMemberInfos = project.getMembers().stream()
                .map(ProjectMemberInfo::new)
                .collect(Collectors.toList());

        long anonymousCount = projectMemberInfos.stream()
                .filter(ProjectMemberInfo::isAnonyVisibility)
                .count();

        return new ProjectDetailInfo(project, projectMemberInfos, anonymousCount);
    }



    @Transactional
    public ProjectAnonyVisibilityUpdateDto updateProjectUserJoinVisibility(int projectId, String email, boolean anonyVisibility) {
        ProjectUserJoin projectUserJoin = projectRepository.getMemberByEmailAndProjectId(projectId, email);

        projectUserJoin.setAnonyVisibility(anonyVisibility);
        projectRepository.saveProjectMember(projectUserJoin);

        return ProjectAnonyVisibilityUpdateDto.builder()
                .projectId(projectUserJoin.getProject().getProjectId())
                .anonyVisibility(projectUserJoin.getAnonyVisibility())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProjectUserJoin> getAllMemberInProject(Integer projectId) { // TODO PeerReviewResponseConverter Facade 사용 필요
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

    private ProjectMemberInfo getProjectMemberInfo(ProjectUserJoin member) {

        if (member.getUser() == null) {
            return ProjectMemberInfo.builder()
                .userId("-1")
                .name(member.getName())
                .email(member.getEmail())
                .roles(new ArrayList<>(member.getRoles()))
                .anonyVisibility(member.getAnonyVisibility())
                .build();
        }

        UserDetailInfo userProfileDetail = userService.getUserDetailInfo(member.getUser().getUserId());
        return ProjectMemberInfo.builder()
            .userId(member.getUser().getUserId())
            .name(userProfileDetail.getUsername())
            .email(userProfileDetail.getEmail())
            .interestDomains(new ArrayList<>(userProfileDetail.getInterestJobs()))
            .introduction(userProfileDetail.getIntroduction())
            .roles(new ArrayList<>(member.getRoles()))
            .projectCount(getProjectCount(member.getUser().getUserId()))
            .anonyVisibility(member.getAnonyVisibility())
            .build();
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
