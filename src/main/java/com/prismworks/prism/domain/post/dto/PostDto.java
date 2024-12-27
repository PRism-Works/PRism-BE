package com.prismworks.prism.domain.post.dto;

import static com.prismworks.prism.domain.post.dto.command.PostCommand.CreatePost;

import com.prismworks.prism.domain.post.dto.command.PostTeamRecruitmentCommand.CreatePostTeamRecruitment;
import com.prismworks.prism.domain.post.dto.command.TeamRecruitmentPositionCommand.CreateTeamRecruitmentPosition;
import com.prismworks.prism.domain.post.dto.query.PostQuery;
import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo;
import com.prismworks.prism.domain.post.model.RecruitmentPostInfo.UserInfo;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import com.prismworks.prism.domain.project.dto.ProjectDetailDto;
import com.prismworks.prism.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class PostDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRecruitmentPostRequest {
        private String title;
        private String content;
        private Integer projectId;
        private ContactMethod contactMethod;
        private String contactInfo;
        private ApplyMethod applyMethod;
        private String applyInfo;
        private ProcessMethod processMethod;
        private boolean isOpenUntilRecruited;
        private LocalDateTime recruitmentStartAt;
        private LocalDateTime recruitmentEndAt;
        private List<RecruitPositionItem> recruitPositions;

        public CreatePost toCreatePostCommand(String userId) {
            return CreatePost.builder()
                .userId(userId)
                .title(this.title)
                .content(this.content)
                .build();
        }

        public CreatePostTeamRecruitment toCreatePostTeamRecruitmentCommand(Post post,
            List<TeamRecruitmentPosition> positions
        ) {
            return CreatePostTeamRecruitment.builder()
                .post(post)
                .projectId(this.projectId)
                .contactMethod(this.contactMethod)
                .contactInfo(this.contactInfo)
                .applyMethod(this.applyMethod)
                .applyInfo(this.applyInfo)
                .processMethod(this.processMethod)
                .recruitmentPositions(positions)
                .isOpenUntilRecruited(this.isOpenUntilRecruited)
                .recruitmentStartAt(this.recruitmentStartAt)
                .recruitmentEndAt(this.recruitmentEndAt)
                .build();
        }

        public List<CreateTeamRecruitmentPosition> toCreateTeamRecruitmentPositionCommand() {
            return recruitPositions.stream()
                .map(positionInfo -> CreateTeamRecruitmentPosition.builder()
                    .position(positionInfo.getPosition())
                    .recruitmentCount(positionInfo.getCount())
                    .build())
                .toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RecruitPositionItem {
        private final RecruitmentPosition position;
        private final int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateRecruitmentPostResponse { // todo: response에 id 추가
        private final Long postId;
        private final LocalDateTime recruitStartAt;
        private final LocalDateTime recruitEndAt;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ContactMethod applyMethod;
        private final String applyInfo;
        private final ProcessMethod processMethod;
        private final List<RecruitPositionItem> recruitPositions;
        private final String title;
        private final String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchRecruitmentPostsRequest {
        private List<RecruitmentPosition> recruitmentPositions;
        private List<Integer> categoryIds;
        private ProcessMethod processMethod;
        private List<String> skills;
        private boolean isRecruiting;
        private int pageNo;
        private int pageSize;

        public PostQuery.GetRecruitmentPosts toGetRecruitmentPostsQuery() {
            return PostQuery.GetRecruitmentPosts.builder()
                .recruitmentPositions(recruitmentPositions)
                .categoryIds(categoryIds)
                .processMethod(processMethod)
                .skills(skills)
                .recruitmentStatuses(isRecruiting ? List.of(RecruitmentStatus.RECRUITING)
                    : List.of(RecruitmentStatus.RECRUITING, RecruitmentStatus.CLOSED))
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class SearchRecruitmentPostItem {
        private final Long postId;
        private final String title;
        private final String content;
        private final List<String> categories;
        private final List<RecruitmentPosition> positions;
        private final boolean isOpenUntilRecruited;
        private final LocalDateTime recruitmentStartAt;
        private final LocalDateTime recruitmentEndAt;
        private final int viewCount;
        private final UserInfo userInfo;

        public SearchRecruitmentPostItem(RecruitmentPostInfo postInfo) {
            this.postId = postInfo.getPostId();
            this.title = postInfo.getTitle();
            this.content = postInfo.getContent();
            this.categories = postInfo.getCategories();
            this.positions = postInfo.getPositions();
            this.isOpenUntilRecruited = postInfo.isOpenUntilRecruited();
            this.recruitmentStartAt = postInfo.getRecruitmentStartAt();
            this.recruitmentEndAt = postInfo.getRecruitmentEndAt();
            this.viewCount = postInfo.getViewCount();
            this.userInfo = postInfo.getUserInfo();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SearchRecruitmentPostsResponse {
        private final long totalCount;
        private final int totalPages;
        private final int currentPage;
        private final List<SearchRecruitmentPostItem> posts;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetMyRecruitmentPostsResponse {
        private final Long postId;
        private final List<RecruitmentPosition> positions;
        private final String title;
        private final List<String> categories;
        private final LocalDateTime recruitEndAt;
        private final int viewCount;
    }

    @Getter
    @Builder
    public static class RecruitmentPostDetailDto {
        private Long postId;
        private Integer projectId;
        private Long postTeamRecruitmentId;
        private final RecruitmentStatus recruitmentStatus;
        private final String title;
        private final String content;
        private final String userId;
        private UserDto.UserProfileDetail writer;
        private final int viewCount;
        private LocalDateTime createdAt;
        private final LocalDateTime recruitmentStart;
        private final LocalDateTime recruitmentEnd;
        private final ProcessMethod processMethod;
        private final List<TeamRecruitmentPosition> recruitmentPositions;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ApplyMethod applicationMethod;
        private final String applicationInfo;

        public static RecruitmentPostDetailDto of(
            Post post,
            PostTeamRecruitment recruitment,
            List<TeamRecruitmentPosition> recruitmentPositions
        ) {
            return RecruitmentPostDetailDto.builder()
                .postId(post.getPostId())
                .projectId(recruitment.getProjectId())
                .postTeamRecruitmentId(recruitment.getPostTeamRecruitmentId())
                .recruitmentStatus(recruitment.getRecruitmentStatus())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .recruitmentStart(recruitment.getRecruitmentStartAt())
                .recruitmentEnd(recruitment.getRecruitmentEndAt())
                .contactMethod(recruitment.getContactMethod())
                .contactInfo(recruitment.getContactInfo())
                .applicationMethod(recruitment.getApplyMethod())
                .applicationInfo(recruitment.getApplyInfo())
                .processMethod(recruitment.getProcessMethod())
                .recruitmentPositions(
                    recruitmentPositions
                )
                .build();
        }

        public void setWriter(UserDto.UserProfileDetail writer) {
            this.writer = writer;
        }
    }

    @Builder
    @Getter
    public static class ViewPostDto {
        private RecruitmentPostDetailDto recruitmentPostDetail;
        private ProjectDetailDto projectDetailDto;
    }
}
