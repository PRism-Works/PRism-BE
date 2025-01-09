package com.prismworks.prism.domain.post.interfaces.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.post.application.PostFacade;
import com.prismworks.prism.domain.post.application.dto.param.SearchRecruitmentPostParam;
import com.prismworks.prism.domain.post.application.dto.param.WritePostParam;
import com.prismworks.prism.domain.post.application.dto.result.SearchRecruitmentPostResult;
import com.prismworks.prism.domain.post.application.dto.result.ViewPostResult;
import com.prismworks.prism.domain.post.application.dto.result.WritPostResult;
import com.prismworks.prism.domain.post.interfaces.dto.request.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.interfaces.dto.request.SearchBookmarkedRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.interfaces.dto.request.SearchRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.interfaces.dto.response.CreateRecruitmentPostResponse;
import com.prismworks.prism.domain.post.interfaces.dto.response.GetRecruitmentPostDetailResponse;
import com.prismworks.prism.domain.post.interfaces.dto.response.SearchRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.interfaces.mapper.PostApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController implements PostControllerDocs {

    private final PostFacade postFacade;
    private final PostApiMapper mapper;

    @GetMapping("/recruitment")
    public ApiSuccessResponse searchRecruitmentPosts(
        @CurrentUser UserContext userContext,
        SearchRecruitmentPostsRequest request
    ) {
        SearchRecruitmentPostParam param = mapper.fromSearchRecruitmentPostRequest(
            request, userContext.getUserId());

        SearchRecruitmentPostResult result = postFacade.searchRecruitmentPost(param);

        SearchRecruitmentPostsResponse response = mapper.toSearchPostsResponse(result);
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/{postId}/recruitment")
    public ApiSuccessResponse getRecruitmentPostDetail(@PathVariable("postId") Long postId) {
        ViewPostResult result = postFacade.viewPost(postId);

        GetRecruitmentPostDetailResponse response = mapper.toGetRecruitmentPostDetailResponse(
            result);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/recruitment")
    public ApiSuccessResponse createRecruitmentPost(@CurrentUser UserContext userContext,
        @RequestBody CreateRecruitmentPostRequest request) {
        WritePostParam param = mapper.fromCreateRecruitmentPostRequest(request, userContext.getUserId());

        WritPostResult result = postFacade.writePost(param);

        CreateRecruitmentPostResponse response = mapper.toCreateRecruitmentPostResponse(result);
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/bookmarks/recruitment")
    public ApiSuccessResponse getBookmarkedRecruitmentPosts(@CurrentUser UserContext userContext,
        SearchBookmarkedRecruitmentPostsRequest request
    ) {
        SearchRecruitmentPostParam param = mapper.fromSearchBookmarkedPostRequest(
            request, userContext.getUserId());

        SearchRecruitmentPostResult result = postFacade.searchRecruitmentPost(param);

        SearchRecruitmentPostsResponse response = mapper.toSearchPostsResponse(result);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/{postId}/bookmarks")
    public ApiSuccessResponse bookmark(@CurrentUser UserContext user, @PathVariable Long postId) {
        postFacade.bookmark(user.getUserId(), postId);
        return ApiSuccessResponse.defaultOk();
    }
}
