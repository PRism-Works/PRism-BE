package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.domain.peerreview.repository.custom.PeerReviewResponseHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerReviewResponseHistoryRepository extends JpaRepository<PeerReviewResponseHistory, Integer>,
        PeerReviewResponseHistoryCustomRepository {
}