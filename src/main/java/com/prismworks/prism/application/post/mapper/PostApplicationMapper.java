package com.prismworks.prism.application.post.mapper;

import com.prismworks.prism.application.post.dto.param.SearchRecruitmentPostParam;
import com.prismworks.prism.application.post.dto.param.WritePostParam;
import com.prismworks.prism.domain.post.dto.command.CreateRecruitmentPostCommand;
import com.prismworks.prism.domain.post.dto.query.GetRecruitmentPostsQuery;
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

    public GetRecruitmentPostsQuery toGetRecruitmentPostsQuery(SearchRecruitmentPostParam param) {
        return GetRecruitmentPostsQuery.builder()
            .recruitmentPositions(param.getRecruitmentPositions())
            .userId(param.getUserId())
            .categoryIds(param.getCategoryIds())
            .processMethod(param.getProcessMethod())
            .skills(param.getSkills())
            .recruitmentStatuses(param.getRecruitmentStatuses())
            .isBookmarkSearch(param.isBookmarkSearch())
            .userId(param.getUserId())
            .pageNo(param.getPageNo())
            .pageSize(param.getPageSize())
            .sort(param.getSort())
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
