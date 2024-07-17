package com.prismworks.prism.domain.search.dto;

import jakarta.ws.rs.DefaultValue;
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
        @DefaultValue(value = "0")
        private final int pageNo;
        @DefaultValue(value = "8")
        private final int pageSize;

        public ProjectSearchCondition getCondition() {
            return ProjectSearchCondition.builder()
                    .searchType(this.searchType)
                    .searchWord(this.searchWord)
                    .categories(this.categories)
                    .build();
        }
    }
}
