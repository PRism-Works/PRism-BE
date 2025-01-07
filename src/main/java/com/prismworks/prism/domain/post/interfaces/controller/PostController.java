package com.prismworks.prism.domain.post.interfaces.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.post.application.PostFacade;
import com.prismworks.prism.domain.post.application.dto.param.WritePostParam;
import com.prismworks.prism.domain.post.domain.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.application.dto.result.ViewPostResult;
import com.prismworks.prism.domain.post.domain.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchBookmarkedRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.SearchRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.interfaces.dto.response.PostResponse.GetRecruitmentPostDetailResponse;
import com.prismworks.prism.domain.post.interfaces.mapper.PostApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final PostApiMapper postApiMapper;

    @GetMapping("/recruitment")
    public ApiSuccessResponse searchRecruitmentPosts(
        @CurrentUser UserContext userContext,
        SearchRecruitmentPostsRequest request
    ) {
        Page<SearchRecruitmentPostInfo> searchResult = postFacade.searchRecruitmentPost(
            request.toGetRecruitmentPostsQuery(userContext.getUserId()));

        SearchRecruitmentPostsResponse response = postApiMapper.toSearchPostsResponse(searchResult);
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/{postId}/recruitment")
    public ApiSuccessResponse getRecruitmentPostDetail(@PathVariable("postId") Long postId) {
        ViewPostResult result = postFacade.viewPost(postId);
        GetRecruitmentPostDetailResponse response = postApiMapper.toGetRecruitmentPostDetailResponse(
            result);

        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/recruitment")
    public ApiSuccessResponse createRecruitmentPost(@CurrentUser UserContext userContext,
        @RequestBody CreateRecruitmentPostRequest request) {
        WritePostParam param = postApiMapper.toWritePostParam(request, userContext.getUserId());
        PostRecruitmentInfo recruitmentPostInfo = postFacade.writePost(param);

        return ApiSuccessResponse.defaultOk(recruitmentPostInfo);
    }

    @GetMapping("/bookmarks/recruitment")
    public ApiSuccessResponse getBookmarkedRecruitmentPosts(@CurrentUser UserContext userContext,
        SearchBookmarkedRecruitmentPostsRequest request
    ) {
        GetRecruitmentPosts query = request.toGetRecruitmentPostsQuery(userContext.getUserId());
        Page<SearchRecruitmentPostInfo> searchResult = postFacade.searchRecruitmentPost(query);

        SearchRecruitmentPostsResponse response = postApiMapper.toSearchPostsResponse(searchResult);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/{postId}/bookmarks")
    public ApiSuccessResponse bookmark(@CurrentUser UserContext user, @PathVariable Long postId) {
        postFacade.bookmark(user.getUserId(), postId);
        return ApiSuccessResponse.defaultOk();
    }
}
