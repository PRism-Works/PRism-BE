package com.prismworks.prism.domain.post.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.post.dto.MyPostCommonFilter;
import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostResponse;
import com.prismworks.prism.domain.post.dto.PostDto.GetMyRecruitmentPostsResponse;
import com.prismworks.prism.domain.post.dto.RecruitmentPostCommonFilter;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProjectPosition;
import com.prismworks.prism.domain.post.model.ProjectProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.post.service.PostService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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
public class PostController {

    @Autowired
    private final PostService service;

    @PostMapping("/recruitment")
    public ApiSuccessResponse createRecruitmentPost(@RequestBody CreateRecruitmentPostRequest request) {
        return ApiSuccessResponse.defaultOk(CreateRecruitmentPostResponse.builder()
            .recruitStartAt(request.getRecruitStartAt())
            .recruitEndAt(request.getRecruitEndAt())
            .contactMethod(request.getContactMethod())
            .contactInfo(request.getContactInfo())
            .applyMethod(request.getApplyMethod())
            .applyInfo(request.getApplyInfo())
            .projectProcessMethod(request.getProjectProcessMethod())
            .recruitPositions(request.getRecruitPositions())
            .title(request.getTitle())
            .content(request.getContent())
            .build());
    }

    @GetMapping("/recruitment/my")
    public ApiSuccessResponse getMyRecruitmentPosts(
        @RequestParam(required = false) RecruitmentPostCommonFilter recruitmentPostCommonFilter,
        @RequestParam MyPostCommonFilter type) {
        return ApiSuccessResponse.defaultOk(GetMyRecruitmentPostsResponse.builder()
            .postId(1L)
            .positions(List.of(ProjectPosition.values()))
            .title("post title")
            .categories(List.of("category1", "category2"))
            .recruitEndAt(LocalDateTime.now())
            .viewCount(10)
            .build());
    }

    @GetMapping("/recruitment/detail/{postId}")
    public ApiSuccessResponse getRecruitmentPostDetail(@PathVariable long postId) {
        PostDto.RecruitmentPostDetailDto response = service.getRecruitmentDetail(postId);

        return ApiSuccessResponse.defaultOk(response);
    }
}
