package com.prismworks.prism.domain.prism.service;

import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResultRepository;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewTotalResultRepository;
import com.prismworks.prism.domain.prism.dto.PrismDataDto;
import com.prismworks.prism.domain.prism.dto.RadialDataDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PrismService {

    private final PeerReviewResultRepository peerReviewResultRepository;
    private final PeerReviewTotalResultRepository peerReviewTotalResultRepository;

    public PrismService(PeerReviewResultRepository peerReviewResultRepository, PeerReviewTotalResultRepository peerReviewTotalResultRepository) {
        this.peerReviewResultRepository = peerReviewResultRepository;
        this.peerReviewTotalResultRepository = peerReviewTotalResultRepository;
    }

    public PrismDataDto calculateUserPrismData(String userId) {
        List<PeerReviewResult> results = peerReviewResultRepository.findByUserId(userId);
        Optional<PeerReviewTotalResult> totalResult = peerReviewTotalResultRepository.findByUserId(userId);

        if (results.isEmpty() && totalResult.isEmpty()) {
            throw new RuntimeException("No data found for user");
        }

        PrismDataDto dto = aggregateResults(results);
        totalResult.ifPresent(tr -> {
            RadialDataDto radialData = new RadialDataDto();
            radialData.setLeadership(Math.round(tr.getLeadershipScore()));
            radialData.setReliability(Math.round(tr.getReliabilityScore()));
            radialData.setTeamwork(Math.round(tr.getTeamworkScore()));
            radialData.setKeywords(tr.getKeywords());
            radialData.setEvaluation(tr.getEvalution());
            dto.setRadialData(radialData);
        });

        return dto;
    }

    public PrismDataDto calculateUserProjectPrismData(String userId, int projectId) {
        List<PeerReviewResult> results = peerReviewResultRepository.findByUserIdAndProjectId(userId, projectId);
        Optional<PeerReviewTotalResult> totalResult = peerReviewTotalResultRepository.findByUserIdAndProjectId(userId, projectId);

        if (results.isEmpty() && totalResult.isEmpty()) {
            throw new RuntimeException("No data found for user and project");
        }

        PrismDataDto dto = aggregateResults(results);
        totalResult.ifPresent(tr -> {
            RadialDataDto radialData = new RadialDataDto();
            radialData.setLeadership(Math.round(tr.getLeadershipScore()));
            radialData.setReliability(Math.round(tr.getReliabilityScore()));
            radialData.setTeamwork(Math.round(tr.getTeamworkScore()));
            radialData.setKeywords(tr.getKeywords());
            radialData.setEvaluation(tr.getEvalution());
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

    private PrismDataDto convertToPrismDataDto(PeerReviewResult result) {
        PrismDataDto dto = new PrismDataDto();
        dto.setPrismData(Map.of(
                "communication", Math.round(result.getCommunicationScore()),
                "proactivity", Math.round(result.getInitiativeScore()),
                "problemSolving", Math.round(result.getProblemSolvingAbilityScore()),
                "responsibility", Math.round(result.getResponsibilityScore()),
                "cooperation", Math.round(result.getTeamworkScore())
        ));
        return dto;
    }
}
