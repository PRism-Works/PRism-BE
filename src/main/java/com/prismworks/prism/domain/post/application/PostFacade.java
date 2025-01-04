package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.dto.PostDto;
import com.prismworks.prism.domain.post.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.model.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import com.prismworks.prism.domain.post.service.PostBookmarkService;
import com.prismworks.prism.domain.post.service.PostService;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class PostFacade {
    private final PostService postService;
    private final PostBookmarkService postBookmarkService;
    private final ProjectService projectService;
    private final UserService userService;

    @Transactional
    public PostRecruitmentInfo writePost(CreateRecruitmentPostRequest req, String userId) {
        return postService.createRecruitmentPost(req, userId);
    }

    @Transactional(readOnly = true)
    public Page<RecruitmentPostInfo> searchRecruitmentPost(GetRecruitmentPosts query) {
        return postService.searchRecruitmentPost(query);
    }

    @Transactional
    public PostDto.ViewPostDto viewPost(Long postId) {

        PostDto.RecruitmentPostDetailDto post = postService.getRecruitmentDetail(postId);

        post.setWriter(userService.getUserProfileDetail(post.getUserId()));

        ProjectDetailDto projectDetailDto = projectService.getProjectDetailInRetrieve(post.getProjectId());

        return PostDto.ViewPostDto.builder()
            .post(post)
            .project(projectDetailDto)
            .build();
    }

    @Transactional
    public void bookmark(String userId, Long postId) {
        Users user = userService.findUserById(userId);
        postBookmarkService.bookmark(user.getUserId(), postId);
    }
}
