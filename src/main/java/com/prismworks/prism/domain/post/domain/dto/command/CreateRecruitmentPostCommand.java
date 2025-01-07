package com.prismworks.prism.domain.post.domain.dto.command;

import com.prismworks.prism.domain.post.domain.model.ApplyMethod;
import com.prismworks.prism.domain.post.domain.model.ContactMethod;
import com.prismworks.prism.domain.post.domain.model.ProcessMethod;
import com.prismworks.prism.domain.post.domain.model.RecruitmentPosition;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateRecruitmentPostCommand {
    private final String userId;
    private final boolean openUntilRecruited;
    private final LocalDateTime recruitmentStartAt;
    private final LocalDateTime recruitmentEndAt;
    private final String title;
    private final String content;
    private final Integer projectId;
    private final ContactMethod contactMethod;
    private final String contactInfo;
    private final ApplyMethod applyMethod;
    private final String applyInfo;
    private final ProcessMethod processMethod;
    private final List<RecruitPositionItem> recruitPositions;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class RecruitPositionItem {
        private final RecruitmentPosition position;
        private final int count;
    }
}
