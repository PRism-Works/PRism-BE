package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.service.PostService;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class PostFacade {
    private final PostService postService;
    private final ProjectService projectService;
    private final UserService userService;

    @Transactional
    public PostDto.ViewPostDto viewPost(Long postId) {

        PostDto.RecruitmentPostDetailDto post = postService.getRecruitmentDetail(postId);

        post.setWriter(userService.getUserProfileDetail(post.getUserId()));

        ProjectDetailDto projectDetailDto = projectService.getProjectDetailInRetrieve(post.getProjectId());

        return PostDto.ViewPostDto.builder()
            .recruitmentPostDetail(post)
            .projectDetailDto(projectDetailDto)
            .build();
    }
}