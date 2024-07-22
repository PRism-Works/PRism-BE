package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResponseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PeerReviewResponseHistoryService {

    private final PeerReviewResponseHistoryRepository peerReviewResponseHistoryRepository;

    @Transactional
    public void saveAllHistories(List<PeerReviewResponseHistory> responses) {
        peerReviewResponseHistoryRepository.saveAll(responses);
    }
}
