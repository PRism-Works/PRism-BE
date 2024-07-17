package com.prismworks.prism.domain.project.Repository.custom.projection;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

public class ProjectProjection {

    @Builder
    @RequiredArgsConstructor
    @Getter
    public static class ProjectSearchResult {
        private final Integer projectId;
        private final String projectName;
        private final String organizationName;
        private final List<String> categories;
        private final Date startDate;
        private final Date endDate;
    }
}
