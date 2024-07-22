package com.prismworks.prism.domain.peerreview.repository.custom;

public interface PeerReviewResponseHistoryCustomRepository {
    Long countReviewerByProjectId(Integer projectId);
}
