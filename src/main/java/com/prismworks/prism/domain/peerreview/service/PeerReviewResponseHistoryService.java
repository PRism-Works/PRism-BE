package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.infrastructure.db.peerreview.PeerReviewResponseHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PeerReviewResponseHistoryService {

    private final PeerReviewResponseHistoryJpaRepository peerReviewResponseHistoryJpaRepository;

    @Transactional
    public void saveAllHistories(List<PeerReviewResponseHistory> responses) {
        peerReviewResponseHistoryJpaRepository.saveAll(responses);
    }

    @Transactional(readOnly = true)
    public Long getReviewerCountInProject(Integer projectId) {
        return peerReviewResponseHistoryJpaRepository.countReviewerByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewResponseHistory> getAllHistoriesByReviewee(String revieweeEmail) {
        return peerReviewResponseHistoryJpaRepository.findAllByRevieweeEmail(revieweeEmail);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewResponseHistory> getAllHistoriesByProject(Integer projectId) {
        return peerReviewResponseHistoryJpaRepository.findAllByProjectId(projectId);
    }
}
