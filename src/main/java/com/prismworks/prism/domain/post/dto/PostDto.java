package com.prismworks.prism.domain.post.dto;

import static com.prismworks.prism.domain.post.dto.command.PostCommand.CreatePost;

import com.prismworks.prism.domain.post.dto.command.PostTeamRecruitmentCommand.CreatePostTeamRecruitment;
import com.prismworks.prism.domain.post.dto.command.TeamRecruitmentPositionCommand.CreateTeamRecruitmentPosition;
import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
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

        public CreatePostTeamRecruitment toCreatePostTeamRecruitmentCommand(Long postId) {
            return CreatePostTeamRecruitment.builder()
                .postId(postId)
                .projectId(this.projectId)
                .contactMethod(this.contactMethod)
                .contactInfo(this.contactInfo)
                .applyMethod(this.applyMethod)
                .applyInfo(this.applyInfo)
                .processMethod(this.processMethod)
                .isOpenUntilRecruited(this.isOpenUntilRecruited)
                .recruitmentStartAt(this.recruitmentStartAt)
                .recruitmentEndAt(this.recruitmentEndAt)
                .build();
        }

        public List<CreateTeamRecruitmentPosition> toCreateTeamRecruitmentPositionCommand(Long postTeamRecruitmentId) {
            return recruitPositions.stream()
                .map(positionInfo -> CreateTeamRecruitmentPosition.builder()
                    .postTeamRecruitmentId(postTeamRecruitmentId)
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
