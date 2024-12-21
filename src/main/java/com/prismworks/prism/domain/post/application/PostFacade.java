package com.prismworks.prism.domain.post.application;

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

    public void viewPost(Long postId) { // 게시글 상세조회
        // todo: implement
    }
}
