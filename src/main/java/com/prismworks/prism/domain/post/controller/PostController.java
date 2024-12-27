package com.prismworks.prism.domain.post.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.post.application.PostFacade;
import com.prismworks.prism.domain.post.dto.MyPostCommonFilter;
import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.dto.PostDto.GetMyRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.dto.PostDto.SearchRecruitmentPostItem;
import com.prismworks.prism.domain.post.dto.PostDto.SearchRecruitmentPostsRequest;
import com.prismworks.prism.domain.post.dto.PostDto.SearchRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.dto.RecruitmentPostCommonFilter;
import com.prismworks.prism.domain.post.model.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import com.prismworks.prism.domain.post.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController implements PostControllerDocs {

    @Autowired
    private final PostFacade postFacade;
    private final PostService postService;

    @PostMapping("/recruitment")
    public ApiSuccessResponse createRecruitmentPost(@CurrentUser UserContext userContext,
        @RequestBody CreateRecruitmentPostRequest request) {

        PostRecruitmentInfo recruitmentPostInfo = postFacade.writePost(request,
            userContext.getUserId());

        return ApiSuccessResponse.defaultOk(recruitmentPostInfo);
    }

    @GetMapping("/recruitment")
    public ApiSuccessResponse searchRecruitmentPosts(
        @CurrentUser UserContext userContext,
        SearchRecruitmentPostsRequest request
    ) {
        System.out.println("보자구");
        System.out.println("request.isRecruiting() : " + request.isRecruiting());
        System.out.println("request.getSkills() : " + request.getSkills());
        System.out.println("request.isBookmarkSearch() : " + request.isBookmarkSearch());
        System.out.println("request.getPageNo() : " + request.getPageNo());
        System.out.println("request.getPageSize() : " + request.getPageSize());
        Page<RecruitmentPostInfo> searchResult = postFacade.searchRecruitmentPost(
            request.toGetRecruitmentPostsQuery(userContext.getUserId()));

        SearchRecruitmentPostsResponse response = SearchRecruitmentPostsResponse.builder()
            .totalCount(searchResult.getTotalElements())
            .totalPages(searchResult.getTotalPages())
            .currentPage(searchResult.getNumber())
            .posts(searchResult.getContent().stream()
                .map(SearchRecruitmentPostItem::new)
                .collect(Collectors.toList()))
            .build();

        return ApiSuccessResponse.defaultOk(response);
    }

    @GetMapping("/recruitment/my")
    public ApiSuccessResponse getMyRecruitmentPosts(
        @RequestParam(required = false) RecruitmentPostCommonFilter recruitmentPostCommonFilter,
        @RequestParam MyPostCommonFilter type) {
        return ApiSuccessResponse.defaultOk(GetMyRecruitmentPostsResponse.builder()
            .postId(1L)
            .positions(List.of(RecruitmentPosition.values()))
            .title("post title")
            .categories(List.of("category1", "category2"))
            .recruitEndAt(LocalDateTime.now())
            .viewCount(10)
            .build());
    }

    @GetMapping("/recruitment/detail")
    public ApiSuccessResponse getRecruitmentPostDetail(@RequestParam("postId") Long postId) {
        PostDto.ViewPostDto response = postFacade.viewPost(postId);

        return ApiSuccessResponse.defaultOk(response);
    }

    @PostMapping("/bookmarks/{postId}")
    public ApiSuccessResponse bookmark(@CurrentUser UserContext user, @PathVariable Long postId) {
        return ApiSuccessResponse.defaultOk(postService.bookmark(user.getUserId(), postId));
    }
}
