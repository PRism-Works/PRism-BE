package com.prismworks.prism.infrastructure.db.peerreview.custom;

public interface PeerReviewResponseHistoryCustomRepository {
    Long countReviewerByProjectId(Integer projectId);
}
