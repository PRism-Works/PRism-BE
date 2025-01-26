package com.prismworks.prism.infrastructure.db.peerreview;

import com.prismworks.prism.domain.peerreview.model.PeerReviewLinkCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeerReviewLinkCodeJpaRepository extends JpaRepository<PeerReviewLinkCode, Integer> {
    Optional<PeerReviewLinkCode> findByCode(String code);

    List<PeerReviewLinkCode> findByProjectIdAndReviewerEmailIn(Integer projectId, List<String> reviewerEmails);
}
