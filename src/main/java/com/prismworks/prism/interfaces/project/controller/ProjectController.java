package com.prismworks.prism.interfaces.project.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.project.dto.*;
import com.prismworks.prism.domain.project.dto.command.CreateProjectCommand;
import com.prismworks.prism.domain.project.dto.command.UpdateProjectCommand;
import com.prismworks.prism.domain.project.dto.query.ProjectInfoQuery;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.interfaces.project.dto.request.ProjectAnonyVisibilityUpdateDto;
import com.prismworks.prism.interfaces.project.dto.request.ProjectInfoRequest;
import com.prismworks.prism.interfaces.project.dto.request.ProjectRequest;
import com.prismworks.prism.interfaces.project.mapper.ProjectApiMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
@Validated
public class ProjectController implements ProjectControllerDocs {

    private final ProjectService projectService;
    private final ProjectApiMapper projectApiMapper;

    @PostMapping
    public ApiSuccessResponse createProject(@CurrentUser UserContext userContext,
                                            @RequestBody @Valid ProjectRequest request) {

        CreateProjectCommand command = projectApiMapper
            .projectRequestToCreateCommand(request, userContext.getEmail());

        ProjectDetailInfo info = projectService.createProject(command);
        return new ApiSuccessResponse(HttpStatus.CREATED.value(), info);
    }

    @PutMapping("/{projectId}")
    public ApiSuccessResponse updateProject(@CurrentUser UserContext userContext,
                                            @PathVariable int projectId,
                                            @RequestBody @Valid ProjectRequest request) {

        UpdateProjectCommand command = projectApiMapper
            .projectRequestToUpdateCommand(request, projectId, userContext.getEmail());

        ProjectDetailInfo info = projectService.updateProject(command);

        return new ApiSuccessResponse(HttpStatus.OK.value(), info);
    }

    @DeleteMapping("/{projectId}")
    public ApiSuccessResponse deleteProject(@CurrentUser UserContext userContext,
                                            @PathVariable int projectId) {
        projectService.deleteProject(userContext.getEmail(),projectId);
        return new ApiSuccessResponse(HttpStatus.NO_CONTENT.value(), null);
    }

    //테스트 완료
    @GetMapping("/summary/by-name")
    public ApiSuccessResponse getProjectSummaryByName(@RequestParam String projectName) {
        List<ProjectInfo> summaryProjects = projectService.getProjectSummaryByName(projectName);
        return new ApiSuccessResponse(HttpStatus.OK.value(), summaryProjects);
    }


    @GetMapping("/summary/by-member-and-filters")
    public ApiSuccessResponse getProjectSummaryByMemberAndFilters(@RequestBody ProjectInfoRequest request) {
        ProjectInfoQuery query = projectApiMapper.projectRequestToInfoQuery(request);
        List<ProjectInfo> summaryProjects = projectService.getProjectSummaryByMemberAndFilters(query);
        return new ApiSuccessResponse(HttpStatus.OK.value(), summaryProjects);
    }

    @GetMapping("/me-involved-projects")
    public ApiSuccessResponse getMeInvolvedProjects(@CurrentUser UserContext userContext) {
        String myEmail = userContext.getEmail();
        List<ProjectInfo> myProjects = projectService.getMeInvolvedProjects(myEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), myProjects);
    }

    //굳이 내가 아니라 다른사람 프로필 검색할 때 프로젝트 리스트 뿌려주는 api
    @GetMapping("/who-involved-projects")
    public ApiSuccessResponse getWhoInvolvedProjects(@RequestParam("userId") String userId) {
        List<ProjectInfo> whosProjects = projectService.getWhoInvolvedProjects(userId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), whosProjects);
    }

    @GetMapping("/me-registered-projects")
    public ApiSuccessResponse getMyRegisteredProjects(@CurrentUser UserContext userContext) {
        String myEmail = userContext.getEmail();
        List<ProjectInfo> myRegisteredProjects = projectService.getMeRegisteredProjects(myEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), myRegisteredProjects);
    }

    @GetMapping("/me-involved-projects/{projectId}")
    public ApiSuccessResponse getProjectDetail(@CurrentUser UserContext userContext,
                                               @PathVariable int projectId) {
        String myEmail = userContext.getEmail();
        ProjectDetailInfo projectDetail = projectService.getProjectDetailInMyPage(myEmail, projectId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectDetail);
    }

    @GetMapping("/summary/detail/{projectId}")
    public ApiSuccessResponse getProjectDetail(@PathVariable int projectId) {
        ProjectDetailInfo projectDetail = projectService.getProjectDetailInRetrieve(projectId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectDetail);
    }

    @PostMapping("/link-project/{projectId}")
    public ApiSuccessResponse linkAnonymousProjectToUserAccount(@CurrentUser UserContext userContext,
                                                                @PathVariable int projectId,
                                                                @RequestParam String anonymousEmail) {
        ProjectDetailInfo projectDetail = projectService.linkAnonymousProjectToUserAccount(userContext, projectId, anonymousEmail);
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectDetail);
    }

    //mypage에서 visibility 설정 하는 api
    @PutMapping("/anonyVisibility")
    public ApiSuccessResponse updateProjectVisibility(@CurrentUser UserContext userContext, @RequestBody ProjectAnonyVisibilityUpdateDto request) {
        ProjectAnonyVisibilityUpdateDto projectVisibilityUpdateDto = projectService.updateProjectUserJoinVisibility(
                request.getProjectId(),
                userContext.getEmail(),
                request.isAnonyVisibility()
        );
        return new ApiSuccessResponse(HttpStatus.OK.value(), projectVisibilityUpdateDto);
    }

    @GetMapping("/{projectId}/members")
    public ApiSuccessResponse getProjectMembers(@PathVariable int projectId) {

        List<MemberDto.MemberDetailDto> result = Arrays.asList(
            MemberDto.MemberDetailDto.builder()
                .userId("아이디")
                .name("일지영")
                .email("이메일이래용")
                .introduction("어떻게 줘야하나")
                .roles(Arrays.asList("개발자", "기획자"))
                .strengths(Arrays.asList("배려", "사고력"))
                .interestDomains(Arrays.asList("금융", "생산성"))
                .joinsProject(3)
                .build()
            ,MemberDto.MemberDetailDto.builder()
                .userId("아이디")
                .name("이지영")
                .email("email")
                .introduction("3년차/재직중")
                .roles(Arrays.asList("백엔드", "프론트"))
                .strengths(Arrays.asList("친화력", "도전정신"))
                .interestDomains(Arrays.asList("통신", "보안"))
                .joinsProject(3)
                .build()
        );

        return ApiSuccessResponse.defaultOk(result);
    }
}
