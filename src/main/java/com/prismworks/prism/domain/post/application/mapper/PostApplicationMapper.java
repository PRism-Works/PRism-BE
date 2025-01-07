package com.prismworks.prism.domain.post.application.mapper;

import com.prismworks.prism.domain.post.application.dto.param.WritePostParam;
import com.prismworks.prism.domain.post.domain.dto.command.CreateRecruitmentPostCommand;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PostApplicationMapper {

    public CreateRecruitmentPostCommand toCreateRecruitmentPostCommand(WritePostParam param) {
        return CreateRecruitmentPostCommand.builder()
            .userId(param.getUserId())
            .openUntilRecruited(param.isOpenUntilRecruited())
            .recruitmentStartAt(param.getRecruitmentStartAt())
            .recruitmentEndAt(param.getRecruitmentEndAt())
            .title(param.getTitle())
            .content(param.getContent())
            .projectId(param.getProjectId())
            .contactMethod(param.getContactMethod())
            .contactInfo(param.getContactInfo())
            .applyMethod(param.getApplyMethod())
            .applyInfo(param.getApplyInfo())
            .processMethod(param.getProcessMethod())
            .recruitPositions(param.getRecruitPositions().stream()
                .map(this::toRecruitPositionItem)
                .collect(Collectors.toList()))
            .build();
    }

    private CreateRecruitmentPostCommand.RecruitPositionItem toRecruitPositionItem(
        WritePostParam.RecruitPositionItem item
    ) {
        return CreateRecruitmentPostCommand.RecruitPositionItem.builder()
            .position(item.getPosition())
            .count(item.getRecruitmentCount())
            .build();
    }
}
