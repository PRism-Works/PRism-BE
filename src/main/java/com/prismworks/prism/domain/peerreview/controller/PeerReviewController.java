package com.prismworks.prism.domain.peerreview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prismworks.prism.common.response.ApiSuccessResponse;
import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.service.PeerReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/peer-review")
@RestController
public class PeerReviewController {

    private final PeerReviewService peerReviewService;

    @PostMapping("/answers")
    public ApiSuccessResponse createPeerReviewAnswer(@RequestBody @Valid PeerReviewDto.CreatePeerReviewResponseRequest request) throws JsonProcessingException {
        peerReviewService.createPeerReviewResponseHistory(request);
        return ApiSuccessResponse.defaultOk();
    }
}
