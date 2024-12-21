package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.service.PostService;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostFacade {
    private final PostService postService;
    private final UserService userService;
    private final ProjectService projectService;

    public PostDto.RecruitmentPostDetailDto viewPost(Long postId) {

        PostDto.RecruitmentPostDetailDto post = postService.getRecruitmentDetail(postId);

        Project project = projectService.getProjectDetails(post.getProjectId());

        //TODO 프로젝트 멤버 데이터 가져오기

        return null;
    }
}
