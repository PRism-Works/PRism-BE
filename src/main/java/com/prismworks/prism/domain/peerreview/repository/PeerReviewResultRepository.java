package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerReviewResultRepository extends JpaRepository<PeerReviewResult, Integer> {
    boolean existsByProjectIdAndEmail(Integer projectId, String email);
}
