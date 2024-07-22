package com.prismworks.prism.domain.peerreview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.service.PeerReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/peer-reviews")
@RestController
public class PeerReviewController {

    private final PeerReviewService peerReviewService;

    @GetMapping("/projects/{projectId}/users/{userId}") // 사용자 프로젝트 동료평가 [마이프로필 - 프로젝트 상세 - 나의 PRism, 나의 PRism분석 리포트]
    public ApiSuccessResponse getProjectPeerReview(@PathVariable Integer projectId, @PathVariable String userId) {
        return ApiSuccessResponse.defaultOk(new PeerReviewDto.ProjectPeerReviewResponse());
    }

    @GetMapping("/users/{userId}") // 사용자 동료평가 [마이프로필 - PRism 종합 리포트]
    public ApiSuccessResponse getOverallPeerReview(@PathVariable String userId) {
        return ApiSuccessResponse.defaultOk(new PeerReviewDto.ProjectPeerReviewResponse());
    }

    @PostMapping("/projects/{projectId}/review-link") // 동료평가 링크 이메일 발송
    public ApiSuccessResponse sendReviewLinkEmail(@PathVariable Integer projectId) {
        return ApiSuccessResponse.defaultOk();
    }

    @PostMapping("/projects/{projectId}/reviews") // 동료평가 응답 등록
    public ApiSuccessResponse createProjectPeerReviews(@PathVariable Integer projectId,
                                                       @RequestBody @Valid PeerReviewDto.CreatePeerReviewResponseRequest request)
    {
        peerReviewService.createPeerReviewResponseHistory(projectId, request);
        return ApiSuccessResponse.defaultOk();
    }

    @PatchMapping("/projects/{projectId}/reviews") // 동료평가 갱신
    public ApiSuccessResponse patchAllPeerReviews(@PathVariable Integer projectId) {
        return ApiSuccessResponse.defaultOk();
    }

    @PatchMapping("/projects/{projectId}/users/{userId}/reviews") // 동료평가 갱신
    public ApiSuccessResponse patchUserPeerReviews(@PathVariable Integer projectId, @PathVariable String userId) {
        return ApiSuccessResponse.defaultOk();
    }
}
