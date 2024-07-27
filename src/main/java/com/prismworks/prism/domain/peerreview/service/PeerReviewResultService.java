package com.prismworks.prism.domain.peerreview.service;

import com.prismworks.prism.domain.peerreview.dto.PeerReviewDto;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.prismworks.prism.domain.peerreview.dto.PeerReviewDto.*;

@RequiredArgsConstructor
@Service
public class PeerReviewResultService {

    private final PeerReviewResultRepository peerReviewResultRepository;

    @Transactional(readOnly = true)
    public Optional<PeerReviewResult> existsPeerReviewResult(Integer projectId, String email) {
        return peerReviewResultRepository.findByProjectIdAndEmail(projectId, email);
    }

    @Transactional
    public void createPeerReviewResult(Integer projectId, String email, ProjectReviewResult projectReviewResult) {
        Optional<PeerReviewResult> peerReviewResultOptional = this.existsPeerReviewResult(projectId, email);
        if(peerReviewResultOptional.isPresent()) {
            PeerReviewResult peerReviewResult = peerReviewResultOptional.get();
            peerReviewResult.updateResult(projectReviewResult);
        }

        PeerReviewResult peerReviewResult = PeerReviewResult.builder()
                .projectId(projectId)
                .email(email)
                .responsibilityScore(projectReviewResult.getResponsibilityScore())
                .initiativeScore(projectReviewResult.getInitiativeScore())
                .problemSolvingAbilityScore(projectReviewResult.getProblemSolvingAbilityScore())
                .teamworkScore(projectReviewResult.getTeamworkScore())
                .communicationScore(projectReviewResult.getCommunicationScore())
                .totalFeedback(projectReviewResult.getTotalFeedback())
                .build();

        peerReviewResultRepository.save(peerReviewResult);
    }
}
