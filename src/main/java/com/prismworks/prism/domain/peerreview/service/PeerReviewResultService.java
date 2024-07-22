package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.peerreview.repository.PeerReviewResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PeerReviewResultService {

    private final PeerReviewResultRepository peerReviewResultRepository;

    @Transactional(readOnly = true)
    public boolean existsPeerReviewResult(Integer projectId, String email) {
        return peerReviewResultRepository.existsByProjectIdAndEmail(projectId, email);
    }

    @Transactional
    public void createPeerReviewResult(Integer projectId, String email) {
        // 1. 이미 해당 이메일과 projectId에 해당하는 결과가 있는지 확인
        // 1-1. 있다면 update
        // 2. 없으면 새로 생성
        if(this.existsPeerReviewResult(projectId, email)) {
            this.updatePeerReviewResult();
            return;
        }

        // todo
    }

    @Transactional
    public void updatePeerReviewResult() {
        // todo
    }
}
