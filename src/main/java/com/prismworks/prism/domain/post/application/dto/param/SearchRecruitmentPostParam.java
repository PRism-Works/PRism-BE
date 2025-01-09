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
    private final List<RecruitmentPosition> recruitmentPositions;
    private final List<Integer> categoryIds;
    private final ProcessMethod processMethod;
    private final List<String> skills;
    private final List<RecruitmentStatus> recruitmentStatuses;
    private final boolean isBookmarkSearch;
    private final String userId;
    private final int pageNo;
    private final int pageSize;
    private final RecruitmentPostSortOption sort;
}
