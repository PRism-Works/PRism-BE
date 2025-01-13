package com.prismworks.prism.infrastructure.db.post.custom.projection;

import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo;
import com.prismworks.prism.domain.post.dto.SearchRecruitmentPostInfo.UserInfo;
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
    private final boolean isBookmarked;

    public SearchRecruitmentPostInfo toRecruitmentPostInfo() {
        Post post = postTeamRecruitment.getPost();
        return SearchRecruitmentPostInfo.builder()
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
            .isBookmarked(isBookmarked)
            .userInfo(new UserInfo(user.getUserId(), user.getUserProfile().getUsername(), user.getEmail()))
            .build();
    }
}
