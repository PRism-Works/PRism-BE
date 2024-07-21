package com.prismworks.prism.domain.peerreview.model;

import com.prismworks.prism.domain.peerreview.dto.ReviewResponse;
import com.prismworks.prism.domain.peerreview.dto.QuestionCategory;
import com.prismworks.prism.domain.peerreview.dto.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PeerReviewResponse {
    private int questionOrder;
    private QuestionType questionType;
    private QuestionCategory questionCategory;
    private ReviewResponse reviewResponse;
}
