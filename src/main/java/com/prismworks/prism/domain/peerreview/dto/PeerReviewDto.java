package com.prismworks.prism.domain.peerreview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class PeerReviewDto {

    @AllArgsConstructor
    @Getter
    public static class CreatePeerReviewResponseRequest {
        private int projectId;
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PeerReviewResponseDetailItem {
        private String revieweeEmail;
        private Object response;
    }
}
