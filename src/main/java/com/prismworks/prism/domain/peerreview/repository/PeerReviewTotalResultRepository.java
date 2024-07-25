package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeerReviewTotalResultRepository extends JpaRepository<PeerReviewTotalResult, Integer> {
    Optional<PeerReviewTotalResult> findByUserId(String userId);
    Optional<PeerReviewTotalResult> findByUserIdAndProjectId(String userId, int projectId);
}
