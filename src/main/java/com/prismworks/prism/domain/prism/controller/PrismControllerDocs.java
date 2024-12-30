package com.prismworks.prism.domain.prism.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "PRism 분석 결과 API", description = "PRism 분석 결과 관련 API")
public interface PrismControllerDocs {
    @Operation(summary = "PRism 분석 리포트 조회", description = "userId에 해당하는 사용자의 PRism 분석 리포트 조회")
    ApiSuccessResponse getUserPrismData(String userId, String prismType);

    @Operation(summary = "프로젝트 PRism 분석 리포트 조회", description = "projectId에 해당하는 프로젝트의 PRism 분석 리포트 조회")
    ApiSuccessResponse getUserProjectPrismData(String userId, Integer projectId, String prismType);
}
