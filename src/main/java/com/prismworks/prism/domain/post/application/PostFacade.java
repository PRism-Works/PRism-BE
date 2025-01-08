package com.prismworks.prism.domain.post.application;

import com.prismworks.prism.domain.post.application.dto.param.SearchRecruitmentPostParam;
import com.prismworks.prism.domain.post.application.dto.param.WritePostParam;
import com.prismworks.prism.domain.post.application.dto.result.SearchRecruitmentPostResult;
import com.prismworks.prism.domain.post.application.dto.result.ViewPostResult;
import com.prismworks.prism.domain.post.application.dto.result.WritPostResult;
import com.prismworks.prism.domain.post.application.mapper.PostApplicationMapper;
import com.prismworks.prism.domain.post.domain.dto.command.CreateRecruitmentPostCommand;
import com.prismworks.prism.domain.post.domain.dto.query.GetRecruitmentPostsQuery;
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
    private final PostApplicationMapper mapper;

    @Transactional
    public WritPostResult writePost(WritePostParam param) {
        CreateRecruitmentPostCommand command = mapper.toCreateRecruitmentPostCommand(param);

        PostRecruitmentInfo postRecruitmentInfo = postService.createRecruitmentPost(command);
        return new WritPostResult(postRecruitmentInfo);
    }

    @Transactional(readOnly = true)
    public SearchRecruitmentPostResult searchRecruitmentPost(SearchRecruitmentPostParam param) {
        GetRecruitmentPostsQuery query = mapper.toGetRecruitmentPostsQuery(param);

        Page<SearchRecruitmentPostInfo> searchRecruitmentPostInfos =
            postService.searchRecruitmentPost(query);

        return new SearchRecruitmentPostResult(searchRecruitmentPostInfos);
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
