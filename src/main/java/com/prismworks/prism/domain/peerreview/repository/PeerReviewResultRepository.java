package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeerReviewResultRepository extends JpaRepository<PeerReviewResult, Integer> {
    boolean existsByProjectIdAndEmail(Integer projectId, String email);

    List<PeerReviewResult> findByUserIdAndPrismType(String userId,String prismType);
    List<PeerReviewResult> findByUserIdAndProjectIdAndPrismType(String userId, int projectId,String prismType);
}
