package com.prismworks.prism.domain.post.repository.custom.projection;

import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo.UserInfo;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import com.prismworks.prism.domain.project.model.Project;
import com.prismworks.prism.domain.user.model.Users;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetPostRecruitmentsProjection {
    private final PostTeamRecruitment postTeamRecruitment;
    private final Project project;
    private final Users user;

    public RecruitmentPostInfo toRecruitmentPostInfo() {
        Post post = postTeamRecruitment.getPost();
        return RecruitmentPostInfo.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(post.getContent())
            .categories(
                project.getCategories().stream()
                    .map(projectCategoryJoin -> projectCategoryJoin.getCategory().getName())
                    .collect(Collectors.toList())
            )
            .positions(
                postTeamRecruitment.getRecruitmentPositions().stream()
                .map(TeamRecruitmentPosition::getPosition)
                .collect(Collectors.toList())
            )
            .isOpenUntilRecruited(postTeamRecruitment.isOpenUntilRecruited())
            .recruitmentStartAt(postTeamRecruitment.getRecruitmentStartAt())
            .recruitmentEndAt(postTeamRecruitment.getRecruitmentEndAt())
            .viewCount(post.getViewCount())
            .userInfo(new UserInfo(user.getUserId(), user.getUserProfile().getUsername(), user.getEmail()))
            .build();
    }
}
