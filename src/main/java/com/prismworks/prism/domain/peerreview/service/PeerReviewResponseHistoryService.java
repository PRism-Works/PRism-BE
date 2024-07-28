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

    @Transactional(readOnly = true)
    public Long getReviewerCountInProject(Integer projectId) {
        return peerReviewResponseHistoryRepository.countReviewerByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewResponseHistory> getAllHistoriesByReviewee(String revieweeEmail) {
        return peerReviewResponseHistoryRepository.findAllByRevieweeEmail(revieweeEmail);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewResponseHistory> getAllHistoriesByProject(Integer projectId) {
        return peerReviewResponseHistoryRepository.findAllByProjectId(projectId);
    }
}
