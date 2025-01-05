package com.prismworks.prism.domain.post.application.dto.command;

import com.prismworks.prism.domain.post.domain.model.RecruitmentPosition;
import lombok.Builder;
import lombok.Getter;

public class TeamRecruitmentPositionCommand {
    @Builder
    @Getter
    public static class CreateTeamRecruitmentPosition {
        private RecruitmentPosition position;
        private Integer recruitmentCount;
    }
}
