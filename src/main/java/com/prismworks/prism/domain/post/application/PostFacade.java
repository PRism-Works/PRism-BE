package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.application.dto.result.ViewPostResult;
import com.prismworks.prism.domain.post.interfaces.dto.PostDto.CreateRecruitmentPostRequest;
import com.prismworks.prism.domain.post.application.dto.query.PostQuery.GetRecruitmentPosts;
import com.prismworks.prism.domain.post.domain.dto.PostRecruitmentInfo;
import com.prismworks.prism.domain.post.domain.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.domain.service.PostBookmarkService;
import com.prismworks.prism.domain.post.domain.service.PostService;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.domain.project.service.ProjectService;
import com.prismworks.prism.domain.user.dto.UserDto.UserProfileDetail;
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
    public Page<SearchRecruitmentPostInfo> searchRecruitmentPost(GetRecruitmentPosts query) {
        return postService.searchRecruitmentPost(query);
    }

    @Transactional
    public ViewPostResult viewPost(Long postId) {
        PostRecruitmentInfo postRecruitmentInfo = postService.getRecruitmentDetail(postId);
        UserProfileDetail userProfileDetail = userService.getUserProfileDetail(
            postRecruitmentInfo.getUserId());
        ProjectDetailDto projectDetailDto = projectService.getProjectDetailInRetrieve(
            postRecruitmentInfo.getProjectId());

        return new ViewPostResult(postRecruitmentInfo, userProfileDetail, projectDetailDto);
    }

    @Transactional
    public void bookmark(String userId, Long postId) {
        Users user = userService.findUserById(userId);
        postBookmarkService.bookmark(user.getUserId(), postId);
    }
}
