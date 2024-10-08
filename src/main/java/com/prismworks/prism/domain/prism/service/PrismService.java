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
            /*
            radialData.setLeadership(tr.getLeadershipScore() != null ? Math.round(tr.getLeadershipScore()) : 0);
            radialData.setReliability(tr.getReliabilityScore() != null ? Math.round(tr.getReliabilityScore()) : 0);
            radialData.setTeamwork(tr.getTeamworkScore() != null ? Math.round(tr.getTeamworkScore()) : 0);
            radialData.setKeywords(tr.getKeywords() != null ? tr.getKeywords() : Collections.emptyList());
            radialData.setEvaluation(tr.getEvalution() != null ? tr.getEvalution() : "");
            */
            radialData.setLeadership(tr.getLeadershipScore() != null ? (float) (Math.floor(tr.getLeadershipScore() * 10) / 10.0) : 0.0f);
            radialData.setReliability(tr.getReliabilityScore() != null ? (float) (Math.floor(tr.getReliabilityScore() * 10) / 10.0) : 0.0f);
            radialData.setTeamwork(tr.getTeamworkScore() != null ? (float) (Math.floor(tr.getTeamworkScore() * 10) / 10.0) : 0.0f);
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
            /*
            radialData.setLeadership(tr.getLeadershipScore() != null ? Math.round(tr.getLeadershipScore()) : 0);
            radialData.setReliability(tr.getReliabilityScore() != null ? Math.round(tr.getReliabilityScore()) : 0);
            radialData.setTeamwork(tr.getTeamworkScore() != null ? Math.round(tr.getTeamworkScore()) : 0);
            radialData.setKeywords(tr.getKeywords() != null ? tr.getKeywords() : Collections.emptyList());
            radialData.setEvaluation(tr.getEvalution() != null ? tr.getEvalution() : "");
            */
            radialData.setLeadership(tr.getLeadershipScore() != null ? (float) (Math.floor(tr.getLeadershipScore() * 10) / 10.0) : 0.0f);
            radialData.setReliability(tr.getReliabilityScore() != null ? (float) (Math.floor(tr.getReliabilityScore() * 10) / 10.0) : 0.0f);
            radialData.setTeamwork(tr.getTeamworkScore() != null ? (float) (Math.floor(tr.getTeamworkScore() * 10) / 10.0) : 0.0f);
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
        for(PrismData prismData : projectPrismDataList) {
            String revieweeEmail = prismData.getRevieweeEmail();
            String revieweeUserId = prismData.getRevieweeUserId();

            // project prism
            PeerReviewResult reviewResult = this.createProjectMemberPeerReviewResult(projectId, "each", prismData);
            PeerReviewTotalResult totalReviewResult = this.createProjectMemberPeerReviewTotalResult(projectId, "each", prismData);

            // user all prism
            List<PeerReviewResult> peerReviewResultsByUser =
                    this.getAllPeerReviewResultsByUser(revieweeEmail, "each");
            List<PeerReviewTotalResult> peerReviewTotalResultsByUser =
                    this.getAllPeerReviewTotalResultsByUser(revieweeEmail, "each");
            peerReviewResultsByUser.add(reviewResult);
            peerReviewTotalResultsByUser.add(totalReviewResult);

            this.createUserPeerReviewResult(revieweeEmail, revieweeUserId, peerReviewResultsByUser);
            this.createUserPeerReviewTotalResult(revieweeEmail, revieweeUserId, peerReviewTotalResultsByUser);
        }
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

        PeerReviewResult peerReviewResult = PeerReviewResult.builder()
                .projectId(projectId)
                .userId(prismData.getRevieweeUserId())
                .email(revieweeEmail)
                .responsibilityScore(prismData.getResponsibilityScore())
                .initiativeScore(prismData.getInitiativeScore())
                .problemSolvingAbilityScore(prismData.getProblemSolvingAbilityScore())
                .cooperationScore(prismData.getCooperationScore())
                .communicationScore(prismData.getCommunicationScore())
                .prismType(prismType)
                .build();

        return peerReviewResultRepository.save(peerReviewResult);
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

        PeerReviewTotalResult peerReviewTotalResult = PeerReviewTotalResult.builder()
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

        return peerReviewTotalResultRepository.save(peerReviewTotalResult);
    }

    @Transactional
    public void createUserPeerReviewResult(String email, String userId, List<PeerReviewResult> peerReviewResults) {
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
            cooperationScore += reviewResult.getCooperationScore();
        }

        Optional<PeerReviewResult> peerReviewResultOptional = this.getPeerReviewResultsByUser(email, "total");
        if(peerReviewResultOptional.isPresent()) {
            PeerReviewResult reviewResult = peerReviewResultOptional.get();
            reviewResult.updateResult(responsibilityScore/size, communicationScore/size, cooperationScore/size,
                    problemSolvingAbilityScore/size, initiativeScore/size);

            return;
        }


        PeerReviewResult peerReviewResult = PeerReviewResult.builder()
                .projectId(null)
                .email(email)
                .userId(userId)
                .responsibilityScore(responsibilityScore / size)
                .initiativeScore(initiativeScore / size)
                .problemSolvingAbilityScore(problemSolvingAbilityScore / size)
                .cooperationScore( cooperationScore / size)
                .communicationScore(communicationScore / size)
                .prismType("total")
                .build();

        peerReviewResultRepository.save(peerReviewResult);
    }

    @Transactional
    public void createUserPeerReviewTotalResult(String email, String userId, List<PeerReviewTotalResult> peerReviewTotalResults) {
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

            return;
        }


        PeerReviewTotalResult peerReviewTotalResult = PeerReviewTotalResult.builder()
                .projectId(null)
                .email(email)
                .userId(userId)
                .leadershipScore(leadershipScore / size)
                .reliabilityScore(reliabilityScore / size)
                .teamworkScore(teamworkScore / size)
                .keywords(keywords)
                .evalution(evaluation)
                .prismType("total")
                .build();

        peerReviewTotalResultRepository.save(peerReviewTotalResult);
    }

    private PrismDataDto aggregateResults(Optional<PeerReviewResult> peerReviewResult) {
        int versionControll = 1;
        Map<String, Float> prismData = Map.of(
                /*
                "communication", Math.round(peerReviewResult.get().getCommunicationScore()),
                "proactivity", Math.round(peerReviewResult.get().getInitiativeScore()),
                "problemSolving", Math.round(peerReviewResult.get().getProblemSolvingAbilityScore()),
                "responsibility", Math.round(peerReviewResult.get().getResponsibilityScore()),
                "cooperation", Math.round(peerReviewResult.get().getCooperationScore())
                */
                "communication", (float) (Math.floor(peerReviewResult.get().getCommunicationScore() * 10) / 10.0),
                "proactivity", (float) (Math.floor(peerReviewResult.get().getInitiativeScore() * 10) / 10.0),
                "problemSolving", (float) (Math.floor(peerReviewResult.get().getProblemSolvingAbilityScore() * 10) / 10.0),
                "responsibility", (float) (Math.floor(peerReviewResult.get().getResponsibilityScore() * 10) / 10.0),
                "cooperation", (float) (Math.floor(peerReviewResult.get().getCooperationScore() * 10) / 10.0)
        );
        PrismDataDto dto = new PrismDataDto();
        dto.setPrismData(prismData);
        return dto;
    }
}
