package com.prismworks.prism.domain.post.application.dto.command;

import com.prismworks.prism.domain.post.domain.model.ApplyMethod;
import com.prismworks.prism.domain.post.domain.model.ContactMethod;
import com.prismworks.prism.domain.post.domain.model.Post;
import com.prismworks.prism.domain.post.domain.model.ProcessMethod;
import com.prismworks.prism.domain.post.domain.model.TeamRecruitmentPosition;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

public class PostTeamRecruitmentCommand {
    @Builder
    @Getter
    public static class CreatePostTeamRecruitment {
        private Post post;
        private Integer projectId;
        private ContactMethod contactMethod;
        private String contactInfo;
        private ApplyMethod applyMethod;
        private String applyInfo;
        private ProcessMethod processMethod;
        private Set<TeamRecruitmentPosition> recruitmentPositions;
        private boolean isOpenUntilRecruited;
        private LocalDateTime recruitmentStartAt;
        private LocalDateTime recruitmentEndAt;
    }
}
