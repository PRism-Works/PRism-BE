package com.prismworks.prism.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageResponse<T> {

    private final long totalCount;
    private final int totalPages;
    private final int currentPage;
    private final List<T> contents;

    public PageResponse(long totalCount, int totalPages, int currentPage, List<T> contents) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.contents = contents;
    }
}
