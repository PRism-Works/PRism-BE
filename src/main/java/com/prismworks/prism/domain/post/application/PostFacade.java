package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.service.PostService;
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

        //TODO post.projectId를 통해서 프로젝트 가져오기
        //TODO post.getPostTeamRecruitmentId() 를 이용하여 RecruitmentPosition 데이터 가져오기

        return null;
    }
}
