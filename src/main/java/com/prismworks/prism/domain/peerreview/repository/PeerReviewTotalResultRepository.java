package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerReviewTotalResultRepository extends JpaRepository<PeerReviewTotalResult, Integer> {
}
