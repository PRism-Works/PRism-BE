package com.prismworks.prism.domain.post.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.post.dto.MyPostCommonFilter;
import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.dto.RecruitmentPostCommonFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시글 API", description = "게시글 관련 API")
public interface PostControllerDocs {

	@Operation(summary = "팀모집 게시글 생성", description = "팀모집을 위한 새로운 게시글 생성한다")
	public ApiSuccessResponse createRecruitmentPost(
		@CurrentUser UserContext userContext,
		@RequestBody PostDto.CreateRecruitmentPostRequest request
	);

	@Operation(summary = "팀모집 게시글 조회", description = "팀모집 게시글을 조회한다")
	@GetMapping("/recruitment")
	public ApiSuccessResponse searchRecruitmentPosts(
		@CurrentUser UserContext userContext,
		PostDto.SearchRecruitmentPostsRequest request
	);

	public ApiSuccessResponse getMyRecruitmentPosts(
		@RequestParam(required = false) RecruitmentPostCommonFilter recruitmentPostCommonFilter,
		@RequestParam MyPostCommonFilter type
	);

	@Operation(summary = "팀모집 게시글 조회", description = "특정 팀모집 게시글을 상세 조회한다")
	public ApiSuccessResponse getRecruitmentPostDetail(@RequestParam("postId") Long postId);

	@Operation(summary = "북마크 표시", description = "게시글에 북마크를 설정/해제 한다")
	public ApiSuccessResponse bookmark(@CurrentUser UserContext user, @PathVariable Long postId);
}
