package com.prismworks.prism.domain.post.application.dto.param;

import com.prismworks.prism.domain.post.domain.dto.query.RecruitmentPostSortOption;
import com.prismworks.prism.domain.post.domain.model.ProcessMethod;
import com.prismworks.prism.domain.post.domain.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.domain.model.RecruitmentStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class SearchRecruitmentPostParam {
    private List<RecruitmentPosition> recruitmentPositions;
    private List<Integer> categoryIds;
    private ProcessMethod processMethod;
    private List<String> skills;
    private List<RecruitmentStatus> recruitmentStatuses;
    private boolean isBookmarkSearch;
    private String userId;
    private int pageNo;
    private int pageSize;
    private RecruitmentPostSortOption sort;
}
