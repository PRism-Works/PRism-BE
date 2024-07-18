package com.prismworks.prism.domain.peerreview.controller;

import com.prismworks.prism.domain.peerreview.service.PeerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/peer-review")
@RestController
public class PeerReviewController {

    private final PeerReviewService peerReviewService;

    @PostMapping("/answers")
    public void createPeerReviewAnswer() {

    }
}
