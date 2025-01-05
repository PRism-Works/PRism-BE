package com.prismworks.prism.domain.post.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class PostRecruitmentInfo {
    private final Long postId;
    private final String userId;
    private final Integer projectId;
    private final String title;
    private final String content;
    private final Integer viewCount;
    private final ContactMethod contactMethod;
    private final String contactInfo;
    private final ApplyMethod applyMethod;
    private final String applyInfo;
    private final ProcessMethod processMethod;
    private final boolean isOpenUntilRecruited;
    private final LocalDateTime recruitmentStartAt;
    private final LocalDateTime recruitmentEndAt;
    private final LocalDateTime createdAt;
    private final List<RecruitmentPositionInfo> recruitmentPositions;

    public PostRecruitmentInfo(Post post, PostTeamRecruitment postTeamRecruitment,
        List<TeamRecruitmentPosition> positions) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.projectId = postTeamRecruitment.getProjectId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.contactMethod = postTeamRecruitment.getContactMethod();
        this.contactInfo = postTeamRecruitment.getContactInfo();
        this.applyMethod = postTeamRecruitment.getApplyMethod();
        this.applyInfo = postTeamRecruitment.getApplyInfo();
        this.processMethod = postTeamRecruitment.getProcessMethod();
        this.isOpenUntilRecruited = postTeamRecruitment.isOpenUntilRecruited();
        this.recruitmentStartAt = postTeamRecruitment.getRecruitmentStartAt();
        this.recruitmentEndAt = postTeamRecruitment.getRecruitmentEndAt();
        this.createdAt = post.getCreatedAt();
        this.recruitmentPositions = positions.stream()
            .map(position -> {
                RecruitmentPositionInfo info = new RecruitmentPositionInfo();
                info.position = position.getPosition();
                info.recruitmentCount = position.getRecruitmentCount();
                return info;
            })
            .toList();
    }

    @Getter
    public static class RecruitmentPositionInfo {
        private RecruitmentPosition position;
        private Integer recruitmentCount;
    }
}
