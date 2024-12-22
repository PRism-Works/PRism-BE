package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.ProjectPosition;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.project.dto.MemberDetailDto;
import com.prismworks.prism.domain.project.dto.ProjectSummaryDto;

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
        private LocalDateTime recruitStartAt;
        private LocalDateTime recruitEndAt;
        private ContactMethod contactMethod;
        private String contactInfo;
        private ContactMethod applyMethod;
        private String applyInfo;
        private ProcessMethod projectProcessMethod;
        private List<RecruitPositionItem> recruitPositions;
        private String title;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class RecruitPositionItem {
        private final ProjectPosition position;
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
        private final ProcessMethod projectProcessMethod;
        private final List<RecruitPositionItem> recruitPositions;
        private final String title;
        private final String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetMyRecruitmentPostsResponse {
        private final Long postId;
        private final List<ProjectPosition> positions;
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
        private final String writer;
        private final int viewCount;
        private LocalDateTime createdAt;
        private final LocalDateTime recruitmentStart;
        private final LocalDateTime recruitmentEnd;
        private final ProcessMethod processMethod;
        private final List<RecruitmentPosition> recruitmentPositions;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ApplyMethod applicationMethod;
        private final String applicationInfo;

        public static RecruitmentPostDetailDto of(Post post, PostTeamRecruitment recruitment, List<RecruitmentPosition> recruitmentPositions) {
            return RecruitmentPostDetailDto.builder()
                .postId(post.getPostId())
                .projectId(recruitment.getProjectId())
                .postTeamRecruitmentId(recruitment.getPostTeamRecruitmentId())
                .recruitmentStatus(recruitment.getRecruitmentStatus())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getUserId())
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
    }

    @Builder
    @Getter
    public static class ViewPostDto {
        private RecruitmentPostDetailDto recruitmentPostDetail;
        private ProjectSummaryDto projectSummary;
        private List<MemberDetailDto> projectMembers;
    }
}
