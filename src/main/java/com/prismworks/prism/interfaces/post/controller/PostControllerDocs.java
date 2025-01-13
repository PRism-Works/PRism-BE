package com.prismworks.prism.interfaces.post.controller;

import com.prismworks.prism.common.response.ApiErrorResponse;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.interfaces.post.dto.request.CreateRecruitmentPostRequest;
import com.prismworks.prism.interfaces.post.dto.request.SearchBookmarkedRecruitmentPostsRequest;
import com.prismworks.prism.interfaces.post.dto.request.SearchRecruitmentPostsRequest;
import com.prismworks.prism.interfaces.post.dto.response.CreateRecruitmentPostResponse;
import com.prismworks.prism.interfaces.post.dto.response.GetRecruitmentPostDetailResponse;
import com.prismworks.prism.interfaces.post.dto.response.SearchRecruitmentPostsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "8. 게시글 API", description = "게시글 관련 API")
public interface PostControllerDocs {
	@Operation(summary = "팀모집 게시글 목록 조회 (로그인 불필요)", description = "팀모집 게시글 목록을 조회한다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "팀모집 게시글 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = SearchRecruitmentPostsResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류 발생",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
	})
	@GetMapping("/recruitment")
	ApiSuccessResponse searchRecruitmentPosts(
		@Parameter(hidden = true) UserContext userContext,
		@ParameterObject SearchRecruitmentPostsRequest request
	);

	@Operation(summary = "팀모집 게시글 상세 조회 (로그인 필요)", description = "특정 팀모집 게시글 상세 조회한다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "팀모집 게시글 상세 조회 성공",
			content = @Content(schema = @Schema(implementation = GetRecruitmentPostDetailResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류 발생",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
	})
	ApiSuccessResponse getRecruitmentPostDetail(Long postId);

	@Operation(summary = "팀모집 게시글 생성 (로그인 필요)", description = "팀모집을 위한 새로운 게시글 생성한다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "팀모집 게시글 생성 성공",
			content = @Content(schema = @Schema(implementation = CreateRecruitmentPostResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류 발생",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
	})
	ApiSuccessResponse createRecruitmentPost(
		@Parameter(hidden = true) UserContext userContext,
		CreateRecruitmentPostRequest request
	);

	@Operation(summary = "북마크 팀모집 게시글 목록 조회 (로그인 필요)", description = "북마크된 팀모집 게시글 목록 조회한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "북마크된 팀모집 게시글 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = SearchRecruitmentPostsResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류 발생",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
	})
	ApiSuccessResponse getBookmarkedRecruitmentPosts(@Parameter(hidden = true) UserContext userContext,
		@ParameterObject SearchBookmarkedRecruitmentPostsRequest request);

	@Operation(summary = "북마크 저장/해제", description = "게시글에 북마크를 저장/해제 한다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 북마크 저장/해제 성공",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류 발생",
			content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
	})
	ApiSuccessResponse bookmark(@Parameter(hidden = true) UserContext user, @PathVariable Long postId);
}
