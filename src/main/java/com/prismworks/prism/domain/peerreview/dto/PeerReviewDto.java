package com.prismworks.prism.domain.peerreview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PeerReviewDto {

    @AllArgsConstructor
    @Getter
    public static class CreateAnswerRequest {
        private final String projectId;
        private final String evaluatorEmail;

    }

    @AllArgsConstructor
    @Getter
    public static class PeerReviewAnswerDetail {
        private final String evaluteeEmail;
    }

    @AllArgsConstructor
    @Getter
    public static class PeerReviewAnswerTest {
        private final Integer questionOrder;
        private final String questionType;
        private final String questionCategory;
        private final String answer;
    }
}
