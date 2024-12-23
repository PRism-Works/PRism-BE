package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.model.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.service.PostService;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.domain.user.dto.UserDto.UserDetail;
import com.prismworks.prism.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PostFacade {
    private final PostService postService;
    private final UserService userService;
    private final ProjectService projectService;

    @Transactional
    public PostRecruitmentInfo writePost(CreateRecruitmentPostRequest req, String userId) {
        return postService.createRecruitmentPost(req, userId);
    }

    public void viewPost(Long postId) { // 게시글 상세조회
        // todo: implement
    }
}
