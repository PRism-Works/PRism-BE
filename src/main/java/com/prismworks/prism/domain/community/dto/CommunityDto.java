package com.prismworks.prism.domain.community.dto;

import com.prismworks.prism.domain.community.model.ContactMethod;
import com.prismworks.prism.domain.community.model.ProjectPosition;
import com.prismworks.prism.domain.community.model.ProjectProcessMethod;
import com.prismworks.prism.domain.community.model.RecruitmentStatus;
import com.prismworks.prism.domain.project.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CommunityDto {
    @Getter
    @AllArgsConstructor
    public static class CreateRecruitmentPostRequest {
        private final LocalDateTime recruitmentStartAt;
        private final LocalDateTime recruitmentEndAt;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ContactMethod applyMethod;
        private final String applyInfo;
        private final ProjectProcessMethod projectProcessMethod;
        private final List<RecruitmentPositionItem> recruitmentPositions;
        private final String title;
        private final String content;
    }

    @Getter
    @AllArgsConstructor
    public static class RecruitmentPositionItem {
        private final ProjectPosition position;
        private final int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateRecruitmentPostResponse { // todo: response에 id 추가
        private final LocalDateTime recruitmentStartAt;
        private final LocalDateTime recruitmentEndAt;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ContactMethod applyMethod;
        private final String applyInfo;
        private final ProjectProcessMethod projectProcessMethod;
        private final List<RecruitmentPositionItem> recruitmentPositions;
        private final String title;
        private final String content;
    }

    @Getter
    @Builder
    public static class RecruitmentPostDetailResponse {
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
        private final List<RecruitmentPositionItem> recruitmentPositions;
        private final ContactMethod contactMethod;
        private final String contactInfo;
        private final ContactMethod applicationMethod;
        private final String applicationInfo;
        private final List<MemberDto.MemberDetailDto> teamMembers;
    }
}
