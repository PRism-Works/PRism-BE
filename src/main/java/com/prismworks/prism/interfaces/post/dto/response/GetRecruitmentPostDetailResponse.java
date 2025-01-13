package com.prismworks.prism.interfaces.post.dto.response;

import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRecruitmentPostDetailResponse {
    private final CommonRecruitmentPostItem post;
    private final CommonProjectItem project;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class CommonRecruitmentPostItem {
        private final Long postId;
        private final String title;
        private final String content;
        private final WriterInfo writer;
        private final int viewCount;
        private final LocalDateTime createdAt;
        private final LocalDateTime recruitmentStartAt;
        private final LocalDateTime recruitmentEndAt;
        private final ProcessMethod processMethod;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ApplyMethod applyMethod;
        private final String applyInfo;
        private final RecruitmentStatus recruitmentStatus;
        private final Set<RecruitmentPositionItem> recruitmentPositions;
    }

    @Getter
    @RequiredArgsConstructor
    public static class RecruitmentPositionItem {
        private final Long positionId;
        private final String positionName;
        private final int recruitmentCount;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class CommonProjectItem {
        private final Integer projectId;
        private final String projectName;
        private final String organizationName;
        private final String startDate;
        private final String endDate;
        private final String projectUrlLink;
        private final boolean urlVisibility;
        private final String projectDescription;
        private final List<String> categories;
        private final List<String> skills;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class WriterInfo {
        private final String userId;
        private final String username;
        private final String email;
    }
}
