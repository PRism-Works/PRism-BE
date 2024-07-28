package com.prismworks.prism.domain.peerreview.repository;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeerReviewResultRepository extends JpaRepository<PeerReviewResult, Integer> {
    Optional<PeerReviewResult> findByProjectIdAndEmail(Integer projectId, String email);
    Optional<PeerReviewResult> findByUserIdAndPrismType(String userId,String prismType);
    //List<PeerReviewResult> findByUserIdAndPrismType(String userId,String prismType);
    Optional<PeerReviewResult> findByUserIdAndProjectIdAndPrismType(String userId, int projectId,String prismType);

    List<PeerReviewResult> findAllByEmailAndPrismType(String email, String prismType);
}
