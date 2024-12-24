package com.prismworks.prism.domain.search.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.search.dto.SearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "검색 API", description = "검색 관련 API")
public interface SearchControllerDocs {
    @Operation(summary = "프로젝트 목록 검색", description = "조건에 맞는 프로젝트 목록 검색")
    ApiSuccessResponse searchProject(SearchDto.ProjectSearchRequest request);
}
