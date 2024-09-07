package com.prismworks.prism.domain.search.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.prismworks.prism.domain.search.dto.ProjectSearchCondition.SearchType;

public class SearchDto {

    @Getter
    @RequiredArgsConstructor
    public static class ProjectSearchRequest {
        private final SearchType searchType;
        private final String searchWord;
        private final Set<Integer> categories;
        private final int pageNo = 0;
        private final int pageSize = 8;

        public ProjectSearchCondition getCondition() {
            return ProjectSearchCondition.builder()
                    .searchType(this.searchType)
                    .searchWord(this.searchWord)
                    .categories(this.categories)
                    .build();
        }
    }
}
