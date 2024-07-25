package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeerReviewResultRepository extends JpaRepository<PeerReviewResult, Integer> {
    boolean existsByProjectIdAndEmail(Integer projectId, String email);

    List<PeerReviewResult> findByUserId(String userId);
    List<PeerReviewResult> findByUserIdAndProjectId(String userId, int projectId);
}
