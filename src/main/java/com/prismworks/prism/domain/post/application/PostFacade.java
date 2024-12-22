package com.prismworks.prism.domain.post.application;


import java.util.List;

import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.service.PostService;
import com.prismworks.prism.domain.project.dto.MemberDetailDto;
import com.prismworks.prism.domain.project.dto.ProjectSummaryDto;
import com.prismworks.prism.domain.project.service.ProjectService;
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

    @Transactional
    public PostDto.ViewPostDto viewPost(Long postId) {

        PostDto.RecruitmentPostDetailDto post = postService.getRecruitmentDetail(postId);
        ProjectSummaryDto project = projectService.getProjectSummary(post.getProjectId());
        List<MemberDetailDto> projectMembers = projectService.getProjectMembers(post.getProjectId());

        return PostDto.ViewPostDto.builder()
            .recruitmentPostDetail(post)
            .projectSummary(project)
            .projectMembers(projectMembers)
            .build();
    }
}
