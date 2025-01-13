package com.prismworks.prism.interfaces.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Builder
@RequiredArgsConstructor
@Getter
public class ProjectSearchCondition {

    public enum SearchType {
        MEMBER_NAME,
        PROJECT_NAME
    }

    private final SearchType searchType;
    private final String searchWord;
    private final Set<Integer> categories;
}
