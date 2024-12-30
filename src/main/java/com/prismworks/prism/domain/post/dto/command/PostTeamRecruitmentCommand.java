package com.prismworks.prism.domain.post.dto.command;

import com.prismworks.prism.domain.post.model.ApplyMethod;
import com.prismworks.prism.domain.post.model.ContactMethod;
import com.prismworks.prism.domain.post.model.Post;
import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.TeamRecruitmentPosition;
import java.time.LocalDateTime;
import java.util.List;
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
        private List<TeamRecruitmentPosition> recruitmentPositions;
        private boolean isOpenUntilRecruited;
        private LocalDateTime recruitmentStartAt;
        private LocalDateTime recruitmentEndAt;
    }
}
