package com.prismworks.prism.infrastructure.db.peerreview;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResponseHistory;
import com.prismworks.prism.infrastructure.db.peerreview.custom.PeerReviewResponseHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeerReviewResponseHistoryJpaRepository extends JpaRepository<PeerReviewResponseHistory, Integer>,
        PeerReviewResponseHistoryCustomRepository {

    List<PeerReviewResponseHistory> findAllByRevieweeEmail(String revieweeEmail);

    List<PeerReviewResponseHistory> findAllByProjectId(Integer projectId);

}
