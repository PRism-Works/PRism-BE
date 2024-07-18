package com.prismworks.prism.domain.peerreview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class PeerReviewQuestion {

    public enum QuestionType {
        OBJECTIVE, SUBJECTIVE
    }

    public enum QuestionCategory {
        LEADERSHIP, ADAPTABILITY, CONCENTRATION, COMMUNICATION, TEAMWORK
    }

    private final Integer questionOrder;
    private final QuestionType questionType;
    private final QuestionCategory questionCategory;
}
