package com.prismworks.prism.interfaces.project.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.interfaces.project.dto.request.ProjectAnonyVisibilityUpdateDto;
import com.prismworks.prism.interfaces.project.dto.request.ProjectInfoRequest;
import com.prismworks.prism.interfaces.project.dto.request.ProjectRequest;
import com.prismworks.prism.interfaces.project.dto.request.UpdateProjectRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

@Tag(name = "프로젝트 API", description = "프로젝트 관련 API")
public interface ProjectControllerDocs {

	@Operation(summary = "프로젝트 생성", description = "새로운 프로젝트 생성")
	ApiSuccessResponse createProject(@Parameter(hidden = true) UserContext userContext,
		@RequestBody @Valid ProjectRequest request) throws ParseException;

	@Operation(summary = "프로젝트 수정", description = "기존 프로젝트 수정")
	ApiSuccessResponse updateProject(@Parameter(hidden = true) UserContext userContext,
		@PathVariable int projectId,
		@RequestBody @Valid UpdateProjectRequest request) throws ParseException;

	@Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
	ApiSuccessResponse deleteProject(@Parameter(hidden = true) UserContext userContext,
		@PathVariable int projectId);

	@Operation(summary = "프로젝트 이름으로 조회 ", description = "프로젝트 이름으로 요약 정보 조회")
	ApiSuccessResponse getProjectSummaryByName(@RequestParam String projectName);

	@Operation(summary = "멤버 및 필터를 통한 프로젝트 조회", description = "멤버와 필터로 프로젝트 요약 정보 조회")
	ApiSuccessResponse getProjectSummaryByMemberAndFilters(
		@RequestBody ProjectInfoRequest request);

	@Operation(summary = "사용자가 참여한 프로젝트 조회", description = "현재 사용자가 참여한 프로젝트 목록 조회")
	ApiSuccessResponse getMeInvolvedProjects(@Parameter(hidden = true) UserContext userContext);

	@Operation(summary = "특정 사용자가 참여한 프로젝트 조회", description = "특정 사용자가 참여한 프로젝트 목록 조회")
	ApiSuccessResponse getWhoInvolvedProjects(@RequestParam("userId") String userId);

	@Operation(summary = "사용자가 등록한 프로젝트 조회", description = "현재 사용자가 등록한 프로젝트 목록 조회")
	ApiSuccessResponse getMyRegisteredProjects(@Parameter(hidden = true) UserContext userContext);

	@Operation(summary = "참여한 프로젝트 상세 조회", description = "현재 사용자가 참여한 특정 프로젝트의 상세 정보 조회")
	ApiSuccessResponse getProjectDetail(@Parameter(hidden = true) UserContext userContext,
		@PathVariable int projectId);

	@Operation(summary = "프로젝트 상세 조회", description = "특정 프로젝트의 상세 정보 조회")
	ApiSuccessResponse getProjectDetail(@PathVariable int projectId);

	@Operation(summary = "익명 프로젝트 계정 연결", description = "익명 프로젝트와 사용자 연결")
	ApiSuccessResponse linkAnonymousProjectToUserAccount(@Parameter(hidden = true) UserContext userContext,
		@PathVariable int projectId,
		@RequestParam String anonymousEmail);

	@Operation(summary = "프로젝트 공개 설정 변경", description = "프로젝트 공개 여부 변경")
	ApiSuccessResponse updateProjectVisibility(@Parameter(hidden = true) UserContext userContext,
		ProjectAnonyVisibilityUpdateDto request);
}
