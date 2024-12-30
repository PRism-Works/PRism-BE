package com.prismworks.prism.domain.post.dto.query;

import com.prismworks.prism.domain.post.model.ProcessMethod;
import com.prismworks.prism.domain.post.model.RecruitmentPosition;
import com.prismworks.prism.domain.post.model.RecruitmentStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostQuery {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class GetRecruitmentPosts {
        private List<RecruitmentPosition> recruitmentPositions;
        private List<Integer> categoryIds;
        private ProcessMethod processMethod;
        private List<String> skills;
        private List<RecruitmentStatus> recruitmentStatuses;
        private boolean isBookmarkSearch;
        private String userId;
        private int pageNo;
        private int pageSize;
    }
}
