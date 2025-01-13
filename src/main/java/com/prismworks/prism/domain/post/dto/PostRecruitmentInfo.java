package com.prismworks.prism.domain.post.dto;

import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.PostTeamRecruitment;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    private final RecruitmentStatus recruitmentStatus;
    private final List<RecruitmentPositionInfo> recruitmentPositions;

    public PostRecruitmentInfo(PostTeamRecruitment postTeamRecruitment) {
        Post post = postTeamRecruitment.getPost();
        Set<TeamRecruitmentPosition> positions = postTeamRecruitment.getRecruitmentPositions();

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
        this.recruitmentStatus = postTeamRecruitment.getRecruitmentStatus();
        this.recruitmentPositions = positions.stream()
            .map(RecruitmentPositionInfo::new)
            .toList();
    }

    @Getter
    public static class RecruitmentPositionInfo {
        private final Long positionId;
        private final RecruitmentPosition position;
        private final int recruitmentCount;

        public RecruitmentPositionInfo(TeamRecruitmentPosition position) {
            this.positionId = position.getPositionId();
            this.position = position.getPosition();
            this.recruitmentCount = position.getRecruitmentCount();
        }
    }
}
