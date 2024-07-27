package com.prismworks.prism.domain.prism.service;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResultRepository;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewTotalResultRepository;
import com.prismworks.prism.domain.prism.dto.PrismDataDto;
import com.prismworks.prism.domain.prism.dto.RadialDataDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrismService {

    private final PeerReviewResultRepository peerReviewResultRepository;
    private final PeerReviewTotalResultRepository peerReviewTotalResultRepository;

    public PrismService(PeerReviewResultRepository peerReviewResultRepository, PeerReviewTotalResultRepository peerReviewTotalResultRepository) {
        this.peerReviewResultRepository = peerReviewResultRepository;
        this.peerReviewTotalResultRepository = peerReviewTotalResultRepository;
    }

    public PrismDataDto calculateUserPrismData(String userId,String prismType) {
        List<PeerReviewResult> results = peerReviewResultRepository.findByUserIdAndPrismType(userId,prismType);
        Optional<PeerReviewTotalResult> totalResult = peerReviewTotalResultRepository.findByUserIdAndPrismType(userId,prismType);

        if (results.isEmpty() && totalResult.isEmpty()) {
            throw new RuntimeException("No data found for user");
        }

        PrismDataDto dto = aggregateResults(results);
        totalResult.ifPresent(tr -> {
            RadialDataDto radialData = new RadialDataDto();
            radialData.setLeadership(tr.getLeadershipScore() != null ? Math.round(tr.getLeadershipScore()) : 0);
            radialData.setReliability(tr.getReliabilityScore() != null ? Math.round(tr.getReliabilityScore()) : 0);
            radialData.setTeamwork(tr.getTeamworkScore() != null ? Math.round(tr.getTeamworkScore()) : 0);
            radialData.setKeywords(tr.getKeywords() != null ? tr.getKeywords() : Collections.emptyList());
            radialData.setEvaluation(tr.getEvalution() != null ? tr.getEvalution() : "");
            dto.setRadialData(radialData);
        });

        return dto;
    }

    public PrismDataDto calculateUserProjectPrismData(String userId, int projectId, String prismType) {
        List<PeerReviewResult> results = peerReviewResultRepository.findByUserIdAndProjectIdAndPrismType(userId, projectId,prismType);
        Optional<PeerReviewTotalResult> totalResult = peerReviewTotalResultRepository.findByUserIdAndProjectIdAndPrismType(userId, projectId,prismType);

        if (results.isEmpty() && totalResult.isEmpty()) {
            throw new RuntimeException("No data found for user and project");
        }

        PrismDataDto dto = aggregateResults(results);
        totalResult.ifPresent(tr -> {
            RadialDataDto radialData = new RadialDataDto();
            radialData.setLeadership(tr.getLeadershipScore() != null ? Math.round(tr.getLeadershipScore()) : 0);
            radialData.setReliability(tr.getReliabilityScore() != null ? Math.round(tr.getReliabilityScore()) : 0);
            radialData.setTeamwork(tr.getTeamworkScore() != null ? Math.round(tr.getTeamworkScore()) : 0);
            radialData.setKeywords(tr.getKeywords() != null ? tr.getKeywords() : Collections.emptyList());
            radialData.setEvaluation(tr.getEvalution() != null ? tr.getEvalution() : "");
            dto.setRadialData(radialData);
        });

        return dto;
    }

    private PrismDataDto aggregateResults(List<PeerReviewResult> results) {
        Map<String, Integer> prismData = Map.of(
                "communication", (int) results.stream().mapToDouble(PeerReviewResult::getCommunicationScore).average().orElse(0.0),
                "proactivity", (int) results.stream().mapToDouble(PeerReviewResult::getInitiativeScore).average().orElse(0.0),
                "problemSolving", (int) results.stream().mapToDouble(PeerReviewResult::getProblemSolvingAbilityScore).average().orElse(0.0),
                "responsibility", (int) results.stream().mapToDouble(PeerReviewResult::getResponsibilityScore).average().orElse(0.0),
                "cooperation", (int) results.stream().mapToDouble(PeerReviewResult::getTeamworkScore).average().orElse(0.0)
        );

        PrismDataDto dto = new PrismDataDto();
        dto.setPrismData(prismData);
        return dto;
    }
}
