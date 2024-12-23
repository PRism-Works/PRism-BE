package com.prismworks.prism.domain.post.dto.command;

import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import lombok.Builder;
import lombok.Getter;

public class TeamRecruitmentPositionCommand {
    @Builder
    @Getter
    public static class CreateTeamRecruitmentPosition {
        private Long postTeamRecruitmentId;
        private RecruitmentPosition position;
        private Integer recruitmentCount;
    }
}
