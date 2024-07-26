package com.prismworks.prism.domain.peerreview.controller;

import com.prismworks.prism.common.annotation.CurrentUser;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.auth.model.UserContext;
import com.prismworks.prism.domain.email.dto.EmailSendRequest;
import com.prismworks.prism.domain.email.model.EmailTemplate;
import com.prismworks.prism.domain.email.service.EmailSendService;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import com.prismworks.prism.domain.peerreview.service.PeerReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/link") // 동료평가 링크 이메일 발송
    public ApiSuccessResponse sendReviewLinkEmail(@CurrentUser UserContext userContext,
                                                  @RequestParam Integer projectId)
    {
        peerReviewService.sendPeerReviewLinkEmail(projectId, userContext.getEmail());
        return ApiSuccessResponse.defaultOk();
    }

    @GetMapping("/link")
    public ApiSuccessResponse getReviewLinkInfo(@RequestParam String code) {
        PeerReviewDto.ReviewLinkInfoResponse response = peerReviewService.getReviewLinkInfo(code);
        return ApiSuccessResponse.defaultOk(response);
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
