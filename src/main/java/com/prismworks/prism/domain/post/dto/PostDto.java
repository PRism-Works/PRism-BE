package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.ProjectPosition;
import com.prismworks.prism.domain.post.model.ProjectProcessMethod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostDto {
    @Getter
    @AllArgsConstructor
    public static class CreateProjectPostRequest {
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
    @AllArgsConstructor
    public static class RecruitPositionItem {
        private final ProjectPosition position;
        private final int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateProjectPostResponse { // todo: response에 id 추가
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
}
