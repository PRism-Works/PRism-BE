package com.prismworks.prism.domain.peerreview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrismData {
    private String revieweeEmail;
    private float communicationScore = 0F;
    private float initiativeScore = 0F;
    private float problemSolvingAbilityScore = 0F;
    private float responsibilityScore = 0F;
    private float cooperationScore = 0F;

    private float teamworkScore = 0F;
    private float leadershipScore = 0F;
    private float reliabilityScore = 0F;
    private PrismSummaryData prismSummaryData;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrismSummaryData {
        private List<String> keywords = new ArrayList<>();
        private String reviewSummary = "";
    }

    public PrismData(String revieweeEmail) {
        this.revieweeEmail = revieweeEmail;
    }

    public void accumulateScore(float communicationScore, float initiativeScore, float problemSolvingAbilityScore,
                                float responsibilityScore, float teamworkScore)
    {
        this.communicationScore += communicationScore;
        this.initiativeScore += initiativeScore;
        this.problemSolvingAbilityScore += problemSolvingAbilityScore;
        this.responsibilityScore += responsibilityScore;
        this.teamworkScore += teamworkScore;
    }

    public void averageScore(int reviewerCount) {
        communicationScore /= reviewerCount;
        initiativeScore /= reviewerCount;
        problemSolvingAbilityScore /= reviewerCount;
        responsibilityScore /= reviewerCount;
        teamworkScore /= reviewerCount;
    }

    public void setReportData(PrismSummaryData summaryData) {
        this.prismSummaryData = summaryData;
        this.calculateReportScore();
    }

    private void calculateReportScore() {
        this.leadershipScore = this.responsibilityScore * 0.4f + this.problemSolvingAbilityScore * 0.3f +
                this.communicationScore * 0.3f;

        this.reliabilityScore = this.responsibilityScore * 0.5f + this.cooperationScore * 0.3f +
                this.communicationScore * 0.2f;

        this.teamworkScore = this.communicationScore * 0.3f + this.initiativeScore * 0.1f +
                this.problemSolvingAbilityScore * 0.2f + this.cooperationScore * 0.3f + this.responsibilityScore * 0.1f;
    }
}
