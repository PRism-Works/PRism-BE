package com.prismworks.prism.domain.post.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.post.dto.MyPostCommonFilter;
import com.prismworks.prism.domain.post.dto.PostDto.CreateProjectPostRequest;
import com.prismworks.prism.domain.post.dto.PostDto.CreateProjectPostResponse;
import com.prismworks.prism.domain.post.dto.PostDto.GetMyProjectPostsResponse;
import com.prismworks.prism.domain.post.dto.ProjectPostCommonFilter;
import com.prismworks.prism.domain.post.model.ProjectPosition;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    @PostMapping("/project")
    public ApiSuccessResponse createProjectPost(@RequestBody CreateProjectPostRequest request) {
        return ApiSuccessResponse.defaultOk(CreateProjectPostResponse.builder()
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

    @GetMapping("/project/my")
    public ApiSuccessResponse getMyProjectPosts(
        @RequestParam(required = false) ProjectPostCommonFilter projectPostCommonFilter,
        @RequestParam MyPostCommonFilter type) {
        return ApiSuccessResponse.defaultOk(GetMyProjectPostsResponse.builder()
            .postId(1L)
            .positions(List.of(ProjectPosition.values()))
            .title("post title")
            .categories(List.of("category1", "category2"))
            .recruitEndAt(LocalDateTime.now())
            .viewCount(10)
            .build());
    }
}
