package com.prismworks.prism.interfaces.search.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.interfaces.search.dto.SearchDto.ProjectSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "검색 API", description = "검색 관련 API")
public interface SearchControllerDocs {
    @Operation(summary = "프로젝트 목록 검색", description = "조건에 맞는 프로젝트 목록 검색")
    ApiSuccessResponse searchProject(ProjectSearchRequest request);
}
