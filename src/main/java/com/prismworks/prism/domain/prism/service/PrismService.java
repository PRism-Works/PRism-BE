package com.prismworks.prism.domain.prism.service;

import com.prismworks.prism.domain.peerreview.exception.PeerReviewException;
import com.prismworks.prism.domain.peerreview.model.PeerReviewResult;
import com.prismworks.prism.domain.peerreview.model.PeerReviewTotalResult;
import com.prismworks.prism.domain.peerreview.model.PrismData;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewResultRepository;
import com.prismworks.prism.domain.peerreview.repository.PeerReviewTotalResultRepository;
import com.prismworks.prism.domain.prism.dto.PrismDataDto;
import com.prismworks.prism.domain.prism.dto.RadialDataDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<PeerReviewResult> results = peerReviewResultRepository.findByUserIdAndPrismType(userId,prismType);
        Optional<PeerReviewTotalResult> totalResult = peerReviewTotalResultRepository.findByUserIdAndPrismType(userId,prismType);

        if (results.isEmpty() && totalResult.isEmpty()) {
            throw PeerReviewException.REVIEW_DATA_NOT_EXIST;
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
        Optional<PeerReviewResult> results = peerReviewResultRepository.findByUserIdAndProjectIdAndPrismType(userId, projectId,prismType);
        Optional<PeerReviewTotalResult> totalResult = peerReviewTotalResultRepository.findByUserIdAndProjectIdAndPrismType(userId, projectId,prismType);

        if (results.isEmpty() && totalResult.isEmpty()) {
            throw PeerReviewException.REVIEW_DATA_NOT_EXIST;
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

    @Transactional(readOnly = true)
    public Optional<PeerReviewResult> getPeerReviewResult(Integer projectId, String email) {
        return peerReviewResultRepository.findByProjectIdAndEmail(projectId, email);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewResult> getAllPeerReviewResultsByUser(String email, String prismType) {
        return peerReviewResultRepository.findAllByEmailAndPrismType(email, prismType);
    }

    @Transactional(readOnly = true)
    public Optional<PeerReviewResult> getPeerReviewResultsByUser(String email, String prismType) {
        return peerReviewResultRepository.findByEmailAndPrismType(email, prismType);
    }

    @Transactional(readOnly = true)
    public Optional<PeerReviewTotalResult> getPeerReviewTotalResult(Integer projectId, String email) {
        return peerReviewTotalResultRepository.findByProjectIdAndEmail(projectId, email);
    }

    @Transactional(readOnly = true)
    public List<PeerReviewTotalResult> getAllPeerReviewTotalResultsByUser(String email, String prismType) {
        return peerReviewTotalResultRepository.findAllByEmailAndPrismType(email, prismType);
    }

    @Transactional(readOnly = true)
    public Optional<PeerReviewTotalResult> getPeerReviewTotalResultsByUser(String email, String prismType) {
        return peerReviewTotalResultRepository.findByEmailAndPrismType(email, prismType);
    }

    @Transactional
    public void refreshPrismData(Integer projectId, List<PrismData> projectPrismDataList) {
        List<PeerReviewResult> peerReviewResults = new ArrayList<>();
        List<PeerReviewTotalResult> peerReviewTotalResults = new ArrayList<>();
        for(PrismData prismData : projectPrismDataList) {
            String revieweeEmail = prismData.getRevieweeEmail();
            String revieweeUserId = prismData.getRevieweeUserId();

            // project prism
            PeerReviewResult reviewResult = this.createProjectMemberPeerReviewResult(projectId, "each", prismData);
            PeerReviewTotalResult totalReviewResult = this.createProjectMemberPeerReviewTotalResult(projectId, "each", prismData);
            peerReviewResults.add(reviewResult);
            peerReviewTotalResults.add(totalReviewResult);

            // user all prism
            List<PeerReviewResult> peerReviewResultsByUser =
                    this.getAllPeerReviewResultsByUser(revieweeEmail, "each");
            List<PeerReviewTotalResult> peerReviewTotalResultsByUser =
                    this.getAllPeerReviewTotalResultsByUser(revieweeEmail, "each");
            peerReviewResultsByUser.add(reviewResult);
            peerReviewTotalResultsByUser.add(totalReviewResult);

            PeerReviewResult userPeerReviewResult =
                    this.createUserPeerReviewResult(revieweeEmail, revieweeUserId, peerReviewResultsByUser);
            PeerReviewTotalResult userPeerReviewTotalResult =
                    this.createUserPeerReviewTotalResult(revieweeEmail, revieweeUserId, peerReviewTotalResultsByUser);
            peerReviewResults.add(userPeerReviewResult);
            peerReviewTotalResults.add(userPeerReviewTotalResult);
        }

        peerReviewResultRepository.saveAll(peerReviewResults);
        peerReviewTotalResultRepository.saveAll(peerReviewTotalResults);
    }

    @Transactional
    public PeerReviewResult createProjectMemberPeerReviewResult(Integer projectId, String prismType, PrismData prismData) {
        String revieweeEmail = prismData.getRevieweeEmail();
        Optional<PeerReviewResult> peerReviewResultOptional = this.getPeerReviewResult(projectId, revieweeEmail);

        if(peerReviewResultOptional.isPresent()) {
            PeerReviewResult peerReviewResult = peerReviewResultOptional.get();
            peerReviewResult.updateResult(prismData);
            return peerReviewResult;
        }

        return PeerReviewResult.builder()
                .projectId(projectId)
                .userId(prismData.getRevieweeUserId())
                .email(revieweeEmail)
                .responsibilityScore(prismData.getResponsibilityScore())
                .initiativeScore(prismData.getInitiativeScore())
                .problemSolvingAbilityScore(prismData.getProblemSolvingAbilityScore())
                .teamworkScore(prismData.getTeamworkScore())
                .communicationScore(prismData.getCommunicationScore())
                .prismType(prismType)
                .build();
    }

    @Transactional
    public PeerReviewTotalResult createProjectMemberPeerReviewTotalResult(Integer projectId, String prismType, PrismData prismData) {
        String revieweeEmail = prismData.getRevieweeEmail();
        Optional<PeerReviewTotalResult> peerReviewResultOptional = this.getPeerReviewTotalResult(projectId, revieweeEmail);

        if(peerReviewResultOptional.isPresent()) {
            PeerReviewTotalResult peerReviewTotalResult = peerReviewResultOptional.get();
            peerReviewTotalResult.updateResult(prismData);
            return peerReviewTotalResult;
        }

        return PeerReviewTotalResult.builder()
                .projectId(projectId)
                .email(revieweeEmail)
                .userId(prismData.getRevieweeUserId())
                .leadershipScore(prismData.getLeadershipScore())
                .reliabilityScore(prismData.getReliabilityScore())
                .teamworkScore(prismData.getTeamworkScore())
                .keywords(prismData.getPrismSummaryData().getKeywords())
                .evalution(prismData.getPrismSummaryData().getReviewSummary())
                .prismType(prismType)
                .build();
    }

    @Transactional
    public PeerReviewResult createUserPeerReviewResult(String email, String userId, List<PeerReviewResult> peerReviewResults) {
        float communicationScore = 0F;
        float initiativeScore = 0F;
        float problemSolvingAbilityScore = 0F;
        float responsibilityScore = 0F;
        float cooperationScore = 0F;
        int size = peerReviewResults.size();
        for(PeerReviewResult reviewResult : peerReviewResults) {
            communicationScore += reviewResult.getCommunicationScore();
            initiativeScore += reviewResult.getInitiativeScore();
            problemSolvingAbilityScore += reviewResult.getProblemSolvingAbilityScore();
            responsibilityScore += reviewResult.getResponsibilityScore();
            cooperationScore += reviewResult.getTeamworkScore();
        }

        Optional<PeerReviewResult> peerReviewResultOptional = this.getPeerReviewResultsByUser(email, "total");
        if(peerReviewResultOptional.isPresent()) {
            PeerReviewResult reviewResult = peerReviewResultOptional.get();
            reviewResult.updateResult(responsibilityScore/size, communicationScore/size, cooperationScore/size,
                    problemSolvingAbilityScore/size, initiativeScore/size);

            return reviewResult;
        }


        return PeerReviewResult.builder()
                .projectId(null)
                .email(email)
                .userId(userId)
                .responsibilityScore(responsibilityScore/size)
                .initiativeScore(initiativeScore/size)
                .problemSolvingAbilityScore(problemSolvingAbilityScore/size)
                .teamworkScore(cooperationScore/size)
                .communicationScore(communicationScore/size)
                .prismType("total")
                .build();
    }

    @Transactional
    public PeerReviewTotalResult createUserPeerReviewTotalResult(String email, String userId, List<PeerReviewTotalResult> peerReviewTotalResults) {
        float teamworkScore = 0F;
        float leadershipScore = 0F;
        float reliabilityScore = 0F;
        int size = peerReviewTotalResults.size();
        PeerReviewTotalResult recentTotalResult = peerReviewTotalResults.get(size - 1);
        List<String> keywords = recentTotalResult.getKeywords();
        String evaluation = recentTotalResult.getEvalution();

        for(PeerReviewTotalResult reviewTotalResult : peerReviewTotalResults) {
            teamworkScore += reviewTotalResult.getTeamworkScore();
            leadershipScore += reviewTotalResult.getLeadershipScore();
            reliabilityScore += reviewTotalResult.getReliabilityScore();
        }

        Optional<PeerReviewTotalResult> reviewTotalResultOptional = this.getPeerReviewTotalResultsByUser(email, "total");
        if(reviewTotalResultOptional.isPresent()) {
            PeerReviewTotalResult reviewTotalResult = reviewTotalResultOptional.get();
            reviewTotalResult.updateResult(reliabilityScore/size, teamworkScore/size, leadershipScore/size,
                    keywords, evaluation);
        }


        return PeerReviewTotalResult.builder()
                .projectId(null)
                .email(email)
                .userId(userId)
                .leadershipScore(leadershipScore/size)
                .reliabilityScore(reliabilityScore/size)
                .teamworkScore(teamworkScore/size)
                .keywords(keywords)
                .evalution(evaluation)
                .prismType("total")
                .build();
    }

    private PrismDataDto aggregateResults(Optional<PeerReviewResult> peerReviewResult) {
        Map<String, Integer> prismData = Map.of(
                "communication", Math.round(peerReviewResult.get().getCommunicationScore()),
                "proactivity", Math.round(peerReviewResult.get().getInitiativeScore()),
                "problemSolving", Math.round(peerReviewResult.get().getProblemSolvingAbilityScore()),
                "responsibility", Math.round(peerReviewResult.get().getResponsibilityScore()),
                "cooperation", Math.round(peerReviewResult.get().getTeamworkScore())
        );
        PrismDataDto dto = new PrismDataDto();
        dto.setPrismData(prismData);
        return dto;
    }
}
