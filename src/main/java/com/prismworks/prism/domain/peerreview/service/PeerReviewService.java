package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.peerreview.repository.PeerReviewAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class PeerReviewService {

    private final PeerReviewAnswerRepository peerReviewAnswerRepository;

    @Transactional
    public void createPeerReviewAnswer() {

    }
}
