package com.prismworks.prism.domain.peerreview.dto;

import lombok.*;

import java.util.List;

public class PeerReviewDto {

    @AllArgsConstructor
    @Getter
    public static class CreatePeerReviewResponseRequest {
        private String reviewerEmail;
        private List<PeerReviewResponseItem> responses;
    }


    @AllArgsConstructor
    @Getter
    public static class PeerReviewResponseItem {
        private int questionOrder;
        private QuestionType questionType;
        private QuestionCategory questionCategory;
        private List<PeerReviewResponseDetailItem> responseDetails;
    }

    @AllArgsConstructor
    @Getter
    public static class PeerReviewResponseDetailItem {
        private String revieweeEmail;
        private Object response;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ProjectPeerReviewResponse {
        private MyPeerReviewResult myPeerReviewResult;
        private MyPeerReviewResultReport myPeerReviewResultReport;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MyPeerReviewResult {
        private double communicationScore;
        private double initiativeScore;
        private double problemSolvingAbilityScore;
        private double responsibilityScore;
        private double teamworkScore;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MyPeerReviewResultReport {
        private double leaderShipScore;
        private double reliabilityScore;
        private double teamworkScore;
        private String reviewSummary;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class ReviewLinkInfoResponse {
        private List<RevieweeInfo> revieweeInfoList;
        private Integer projectId;
        private String reviewerEmail;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class RevieweeInfo {
        private String revieweeEmail;
        private String revieweeName;
    }
}
