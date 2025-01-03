package com.prismworks.prism.domain.post.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.post.application.PostFacade;
import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.dto.PostDto.SearchBookmarkedRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.dto.PostDto.SearchRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.dto.PostDto.SearchRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.mapper.PostMapper;
import com.prismworks.prism.domain.post.model.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
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
    private final PostMapper postMapper;

    @GetMapping("/recruitment")
    public ApiSuccessResponse searchRecruitmentPosts(
        @CurrentUser UserContext userContext,
        SearchRecruitmentPostsRequest request
    ) {
        Page<RecruitmentPostInfo> searchResult = postFacade.searchRecruitmentPost(
            request.toGetRecruitmentPostsQuery(userContext.getUserId()));

        SearchRecruitmentPostsResponse response = postMapper.toSearchPostsResponse(searchResult);
        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/{postId}/recruitment")
    public ApiSuccessResponse getRecruitmentPostDetail(@PathVariable("postId") Long postId) {
        PostDto.ViewPostDto response = postFacade.viewPost(postId);

        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/recruitment")
    public ApiSuccessResponse createRecruitmentPost(@CurrentUser UserContext userContext,
        @RequestBody CreateRecruitmentPostRequest request) {

        PostRecruitmentInfo recruitmentPostInfo = postFacade.writePost(request,
            userContext.getUserId());

        return ApiSuccessResponse.defaultOk(recruitmentPostInfo);
    }

    @GetMapping("/bookmarks/recruitment")
    public ApiSuccessResponse getBookmarkedRecruitmentPosts(@CurrentUser UserContext userContext,
        SearchBookmarkedRecruitmentPostsRequest request
    ) {
        GetRecruitmentPosts query = request.toGetRecruitmentPostsQuery(userContext.getUserId());
        Page<RecruitmentPostInfo> searchResult = postFacade.searchRecruitmentPost(query);

        SearchRecruitmentPostsResponse response = postMapper.toSearchPostsResponse(searchResult);
        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/{postId}/bookmarks")
    public ApiSuccessResponse bookmark(@CurrentUser UserContext user, @PathVariable Long postId) {
        postFacade.bookmark(user.getUserId(), postId);
        return ApiSuccessResponse.defaultOk();
    }
}
