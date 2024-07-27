package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeerReviewTotalResultRepository extends JpaRepository<PeerReviewTotalResult, Integer> {
    Optional<PeerReviewTotalResult> findByUserIdAndPrismType(String userId,String prismType);
    Optional<PeerReviewTotalResult> findByUserIdAndProjectIdAndPrismType(String userId, int projectId,String prismType);
}
