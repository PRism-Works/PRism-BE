package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProjectPosition;
import com.prismworks.prism.domain.post.model.ProjectProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;

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
        private ProjectProcessMethod projectProcessMethod;
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
        private final ProjectProcessMethod projectProcessMethod;
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
        private final RecruitmentStatus recruitmentStatus;
        private final String title;
        private final String content;
        private final String writer;
        private final int viewCount;
        private final String projectUrlLink;
        private final String projectDescription;
        private final List<String> categories;
        private final List<String> skills;
        private final LocalDateTime recruitmentStartAt;
        private final LocalDateTime recruitmentEndAt;
        private final ProjectProcessMethod projectProcessMethod;
        private final List<RecruitPositionItem> recruitmentPositions;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ContactMethod applicationMethod;
        private final String applicationInfo;
    }

    @Getter
    @Builder
    public static class RecruitmentPostResponseDto {
        private final Long postId;
        private final String content;
        private final List<RecruitPositionItem> positions;
        private final List<String> categories;
        private final LocalDateTime recruitmentEndAt;
        private final int viewCount;
    }
}
