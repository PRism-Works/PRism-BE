package com.prismworks.prism.domain.peerreview.controller;

import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "동료평가 API", description = "동료평가 관련 API")
public interface PeerReviewControllerDocs {
    @Operation(summary = "동료평가 링크 이메일 발송", description = "projectId에 해당하는 프로젝트의 참여자들에게 동료평가 링크 이메일 발송")
     ApiSuccessResponse sendReviewLinkEmail(@Parameter(hidden = true) UserContext userContext,
         Integer projectId);

    @Operation(summary = "동료평가 링크 정보 조회", description = "동료평가 링크에 있는 code를 통해 동료평가 링크 정보 조회")
    ApiSuccessResponse getReviewLinkInfo(String code);

    @Operation(summary = "동료평가 응답 등록", description = "동료평가 응답 등록")
    ApiSuccessResponse createProjectPeerReviews(Integer projectId,
        PeerReviewDto.CreatePeerReviewResponseRequest request);

    @Operation(summary = "동료평가 갱신", description = "동료평가 갱신")
    ApiSuccessResponse patchAllPeerReviews(Integer projectId);
}
